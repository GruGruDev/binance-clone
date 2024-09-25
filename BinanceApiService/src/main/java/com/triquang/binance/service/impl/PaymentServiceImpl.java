package com.triquang.binance.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.triquang.binance.domain.PaymentMethod;
import com.triquang.binance.domain.PaymentOrderStatus;
import com.triquang.binance.model.PaymentOrder;
import com.triquang.binance.model.User;
import com.triquang.binance.repository.PaymentOrderRepository;
import com.triquang.binance.response.PaymentResponse;
import com.triquang.binance.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {
	@Autowired
	private PaymentOrderRepository paymentOrderRepository;

	@Value("${stripe.api.key}")
	private String stripeKey;

	@Value("${paypal.client.id}")
	private String paypalClientId;

	@Value("${paypal.client.secret}")
	private String paypalClientSecret;

	@Value("${paypal.mode}")
	private String paypalMode;

	@Override
	public PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod) {
		PaymentOrder order = new PaymentOrder();
		order.setUser(user);
		order.setAmount(amount);
		order.setPaymentMethod(paymentMethod);
		return paymentOrderRepository.save(order);
	}

	@Override
	public PaymentOrder getPaymentOrderById(Long id) throws Exception {
		Optional<PaymentOrder> optionalPaymentOrder = paymentOrderRepository.findById(id);
		if (optionalPaymentOrder.isEmpty()) {
			throw new Exception("payment order not found with id " + id);
		}
		return optionalPaymentOrder.get();
	}

	@Override
	public Boolean proccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws PayPalRESTException {
		if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
			
			paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
			paymentOrderRepository.save(paymentOrder);

			return true;
		}

		return false;
	}

	@Override
	public PaymentResponse createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException {
		Stripe.apiKey = stripeKey;

		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.setMode(SessionCreateParams.Mode.PAYMENT)
				.setSuccessUrl("http://localhost:5173/wallet?order_id=" + orderId)
				.setCancelUrl(
						"http://localhost:5173/payment/cancel")
				.addLineItem(
						SessionCreateParams.LineItem.builder().setQuantity(1L)
								.setPriceData(
										SessionCreateParams.LineItem.PriceData.builder().setCurrency("usd")
												.setUnitAmount(amount * 100)
												.setProductData(SessionCreateParams.LineItem.PriceData.ProductData
														.builder().setName("Top up wallet").build())
												.build())
								.build())
				.build();

		Session session = Session.create(params);

		System.out.println("session _____ " + session);

		PaymentResponse res = new PaymentResponse();
		res.setPayment_url(session.getUrl());

		return res;
	}

	@Override
	public PaymentResponse createPayPalPaymentLink(User user, Long amount, Long orderId) throws PayPalRESTException {
		APIContext apiContext = new APIContext(paypalClientId, paypalClientSecret, paypalMode);

		// Create amount details
		Amount paymentAmount = new Amount();
		paymentAmount.setCurrency("USD"); // Change to the appropriate currency
		paymentAmount.setTotal(String.format("%.2f", amount / 100.0));

		// Create transaction details
		Transaction transaction = new Transaction();
		transaction.setDescription("Payment for Order ID: " + orderId);
		transaction.setAmount(paymentAmount);

		List<Transaction> transactions = new ArrayList<>();
		transactions.add(transaction);

		// Create payer details
		Payer payer = new Payer();
		payer.setPaymentMethod("paypal");

		// Create redirect URLs
		RedirectUrls redirectUrls = new RedirectUrls();
		redirectUrls.setCancelUrl("http://localhost:5173/wallet/cancel");
		redirectUrls.setReturnUrl("http://localhost:5173/wallet/" + orderId);

		// Create payment details
		com.paypal.api.payments.Payment payment = new com.paypal.api.payments.Payment();
		payment.setIntent("sale");
		payment.setPayer(payer);
		payment.setTransactions(transactions);
		payment.setRedirectUrls(redirectUrls);

		// Create the payment
		com.paypal.api.payments.Payment createdPayment = payment.create(apiContext);

		// Extract approval URL
		String approvalUrl = null;
		for (Links link : createdPayment.getLinks()) {
			if ("approval_url".equals(link.getRel())) {
				approvalUrl = link.getHref();
				break;
			}
		}

		// Create and return payment response
		PaymentResponse res = new PaymentResponse();
		res.setPayment_url(approvalUrl);

		return res;

	}

}
