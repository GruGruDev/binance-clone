package com.triquang.binance.service;

import com.triquang.binance.domain.VerificationType;
import com.triquang.binance.model.ForgotPasswordToken;
import com.triquang.binance.model.User;

public interface ForgotPasswordService {
	ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType, String sendTo);

	ForgotPasswordToken findById(String id);

	ForgotPasswordToken findByUser(Long userId);

	void deleteToken(ForgotPasswordToken token);

	boolean verifyToken(ForgotPasswordToken token, String otp);
}
