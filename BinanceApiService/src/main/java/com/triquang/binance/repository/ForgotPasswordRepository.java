package com.triquang.binance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.binance.model.ForgotPasswordToken;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPasswordToken, String> {
	ForgotPasswordToken findByUserId(Long userId);
}
