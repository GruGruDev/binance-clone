package com.triquang.binance.service;

import com.triquang.binance.domain.VerificationType;
import com.triquang.binance.model.User;
import com.triquang.binance.model.VerificationCode;

public interface VerificationService {
	VerificationCode sendVerificationOTP(User user, VerificationType verificationType);

	VerificationCode findVerificationById(Long id) throws Exception;

	VerificationCode findUsersVerification(User user) throws Exception;

	Boolean VerifyOtp(String opt, VerificationCode verificationCode);

	void deleteVerification(VerificationCode verificationCode);
}
