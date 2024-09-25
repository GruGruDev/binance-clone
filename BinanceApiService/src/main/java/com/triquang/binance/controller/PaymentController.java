package com.triquang.binance.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.paypal.base.rest.PayPalRESTException;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import com.triquang.binance.domain.PaymentMethod;
import com.triquang.binance.exception.UserException;
import com.triquang.binance.model.PaymentOrder;
import com.triquang.binance.model.User;
import com.triquang.binance.response.PaymentResponse;
import com.triquang.binance.service.PaymentService;
import com.triquang.binance.service.UserService;

@RestController
public class PaymentController {
	@Autowired
	private UserService userService;

	@Autowired
	private PaymentService paymentService;

	@PostMapping("/api/payment/{paymentMethod}/amount/{amount}")
	public ResponseEntity<PaymentResponse> paymentHandler(@PathVariable PaymentMethod paymentMethod,
			@PathVariable Long amount, @RequestHeader("Authorization") String jwt)
			throws UserException, RazorpayException, StripeException, PayPalRESTException, IOException {

		User user = userService.findUserProfileByJwt(jwt);

		PaymentResponse paymentResponse;

		PaymentOrder order = paymentService.createOrder(user, amount, paymentMethod);

		if (paymentMethod.equals(PaymentMethod.PAYPAL)) {
			paymentResponse = paymentService.createPayPalPaymentLink(user, amount, order.getId());
		} else {
			paymentResponse = paymentService.createStripePaymentLink(user, amount, order.getId());
		}

		return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
	}
}
