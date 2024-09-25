package com.triquang.binance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.binance.model.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
	Wallet findByUserId(Long userId);
}
