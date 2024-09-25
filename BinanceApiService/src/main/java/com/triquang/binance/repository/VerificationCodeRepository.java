package com.triquang.binance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.binance.model.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
	public VerificationCode findByUserId(Long userId);
}
