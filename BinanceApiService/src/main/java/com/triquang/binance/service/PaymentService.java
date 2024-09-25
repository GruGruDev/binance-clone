package com.triquang.binance.service;

import com.paypal.base.rest.PayPalRESTException;
import com.stripe.exception.StripeException;
import com.triquang.binance.domain.PaymentMethod;
import com.triquang.binance.model.PaymentOrder;
import com.triquang.binance.model.User;
import com.triquang.binance.response.PaymentResponse;

public interface PaymentService {
	PaymentOrder createOrder(User user, Long amount, PaymentMethod paymentMethod);

	PaymentOrder getPaymentOrderById(Long id) throws Exception;

	Boolean proccedPaymentOrder(PaymentOrder paymentOrder, String paymentId) throws PayPalRESTException;

	PaymentResponse createStripePaymentLink(User user, Long Amount, Long orderId) throws StripeException;

	PaymentResponse createPayPalPaymentLink(User user, Long amount, Long orderId) throws PayPalRESTException;
}
