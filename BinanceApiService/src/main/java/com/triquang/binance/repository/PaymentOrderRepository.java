package com.triquang.binance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.binance.model.PaymentOrder;

public interface PaymentOrderRepository extends JpaRepository<PaymentOrder, Long> {

}
