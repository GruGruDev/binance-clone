package com.triquang.binance.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triquang.binance.domain.VerificationType;
import com.triquang.binance.model.ForgotPasswordToken;
import com.triquang.binance.model.User;
import com.triquang.binance.repository.ForgotPasswordRepository;
import com.triquang.binance.service.ForgotPasswordService;

import java.util.Optional;

@Service
public class ForgotPasswordServiceImpl implements ForgotPasswordService {
	@Autowired
	private ForgotPasswordRepository forgotPasswordRepository;

	@Override
	public ForgotPasswordToken createToken(User user, String id, String otp, VerificationType verificationType,
			String sendTo) {
		var token = new ForgotPasswordToken();
		token.setUser(user);
		token.setId(id);
		token.setOtp(otp);
		token.setVerificationType(verificationType);
		token.setSendTo(sendTo);

		return forgotPasswordRepository.save(token);
	}

	@Override
	public ForgotPasswordToken findById(String id) {
		Optional<ForgotPasswordToken> opt = forgotPasswordRepository.findById(id);
		return opt.orElse(null);
	}

	@Override
	public ForgotPasswordToken findByUser(Long userId) {
		return forgotPasswordRepository.findByUserId(userId);
	}

	@Override
	public void deleteToken(ForgotPasswordToken token) {
		forgotPasswordRepository.delete(token);
	}

	@Override
	public boolean verifyToken(ForgotPasswordToken token, String otp) {
		return token.getOtp().equals(otp);
	}
}
