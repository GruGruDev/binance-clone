package com.triquang.binance.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triquang.binance.domain.WalletTransactionType;
import com.triquang.binance.model.Wallet;
import com.triquang.binance.model.WalletTransaction;
import com.triquang.binance.repository.WalletTransactionRepository;
import com.triquang.binance.service.WalletTransactionService;

@Service
public class WalletTransactionServiceImpl implements WalletTransactionService {

	@Autowired
	private WalletTransactionRepository walletTransactionRepository;

	@Override
	public WalletTransaction createTransaction(Wallet wallet, WalletTransactionType type, String transferId,
			String purpose, Long amount) {
		WalletTransaction transaction = new WalletTransaction();
		transaction.setWallet(wallet);
		transaction.setDate(LocalDate.now());
		transaction.setType(type);
		transaction.setTransferId(transferId);
		transaction.setPurpose(purpose);
		transaction.setAmount(amount);

		return walletTransactionRepository.save(transaction);
	}

	@Override
	public List<WalletTransaction> getTransactions(Wallet wallet, WalletTransactionType type) {
		return walletTransactionRepository.findByWalletOrderByDateDesc(wallet);
	}
}
