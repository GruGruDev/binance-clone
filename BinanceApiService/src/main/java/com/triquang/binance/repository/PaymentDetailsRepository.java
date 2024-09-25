package com.triquang.binance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.binance.model.PaymentDetails;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails, Long> {
	PaymentDetails getPaymentDetailsByUserId(Long userId);

}
