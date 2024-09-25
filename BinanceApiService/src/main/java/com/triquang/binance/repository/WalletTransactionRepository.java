package com.triquang.binance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.binance.model.Wallet;
import com.triquang.binance.model.WalletTransaction;

public interface WalletTransactionRepository extends JpaRepository<WalletTransaction, Long> {

	List<WalletTransaction> findByWalletOrderByDateDesc(Wallet wallet);

}
