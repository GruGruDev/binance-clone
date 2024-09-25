package com.triquang.binance.service;

import com.triquang.binance.model.PaymentDetails;
import com.triquang.binance.model.User;

public interface PaymentDetailsService {
	public PaymentDetails addPaymentDetails(String accountNumber, String accountHolderName, String ifsc,
			String bankName, User user);

	public PaymentDetails getUsersPaymentDetails(User user);
}
