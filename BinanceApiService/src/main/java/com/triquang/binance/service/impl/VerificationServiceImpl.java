package com.triquang.binance.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triquang.binance.domain.VerificationType;
import com.triquang.binance.model.User;
import com.triquang.binance.model.VerificationCode;
import com.triquang.binance.repository.VerificationCodeRepository;
import com.triquang.binance.service.VerificationService;
import com.triquang.binance.utils.OtpUtils;

@Service
public class VerificationServiceImpl implements VerificationService {
	@Autowired
	private VerificationCodeRepository verificationRepository;

	@Override
	public VerificationCode sendVerificationOTP(User user, VerificationType verificationType) {

		VerificationCode verificationCode = new VerificationCode();

		verificationCode.setOtp(OtpUtils.generateOTP());
		verificationCode.setUser(user);
		verificationCode.setVerificationType(verificationType);

		return verificationRepository.save(verificationCode);
	}

	@Override
	public VerificationCode findVerificationById(Long id) throws Exception {
		Optional<VerificationCode> verificationCodeOption = verificationRepository.findById(id);
		if (verificationCodeOption.isEmpty()) {
			throw new Exception("verification not found");
		}
		return verificationCodeOption.get();
	}

	@Override
	public VerificationCode findUsersVerification(User user) throws Exception {
		return verificationRepository.findByUserId(user.getId());
	}

	@Override
	public Boolean VerifyOtp(String opt, VerificationCode verificationCode) {
		return opt.equals(verificationCode.getOtp());
	}

	@Override
	public void deleteVerification(VerificationCode verificationCode) {
		verificationRepository.delete(verificationCode);
	}
}
