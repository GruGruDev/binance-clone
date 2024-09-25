package com.triquang.binance.service.impl;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triquang.binance.model.TwoFactorOTP;
import com.triquang.binance.model.User;
import com.triquang.binance.repository.TwoFactorOTPRepository;
import com.triquang.binance.service.TwoFactorOTPService;

@Service
public class TwoFactorOTPServiceImpl implements TwoFactorOTPService {
	@Autowired
	private TwoFactorOTPRepository twoFactorOtpRepository;

	@Override
	public TwoFactorOTP createTwoFactorOtp(User user, String otp, String jwt) {
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();

		TwoFactorOTP twoFactorOTP = new TwoFactorOTP();
		twoFactorOTP.setId(id);
		twoFactorOTP.setUser(user);
		twoFactorOTP.setOtp(otp);
		twoFactorOTP.setJwt(jwt);
		return twoFactorOtpRepository.save(twoFactorOTP);

	}

	@Override
	public TwoFactorOTP findByUser(Long userId) {
		return twoFactorOtpRepository.findByUserId(userId);
	}

	@Override
	public TwoFactorOTP findById(String id) {
		Optional<TwoFactorOTP> twoFactorOtp = twoFactorOtpRepository.findById(id);
		return twoFactorOtp.orElse(null);
	}

	@Override
	public boolean verifyTwoFactorOtp(TwoFactorOTP twoFactorOtp, String otp) {
		return twoFactorOtp.getOtp().equals(otp);
	}

	@Override
	public void deleteTwoFactorOtp(TwoFactorOTP twoFactorOTP) {
		twoFactorOtpRepository.delete(twoFactorOTP);
	}

}
