package com.triquang.binance.service;

import com.triquang.binance.model.TwoFactorOTP;
import com.triquang.binance.model.User;

public interface TwoFactorOTPService {
	TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt);

	TwoFactorOTP findByUser(Long userId);

	TwoFactorOTP findById(String id);

	boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp);

	void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP);
}
