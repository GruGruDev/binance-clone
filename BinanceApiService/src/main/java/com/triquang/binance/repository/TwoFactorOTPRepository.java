package com.triquang.binance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.binance.model.TwoFactorOTP;

public interface TwoFactorOTPRepository extends JpaRepository<TwoFactorOTP, String> {
	TwoFactorOTP findByUserId(Long userId);

}
