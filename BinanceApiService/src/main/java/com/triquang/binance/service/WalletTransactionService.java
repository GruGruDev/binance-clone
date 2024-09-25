package com.triquang.binance.service;

import java.util.List;

import com.triquang.binance.domain.WalletTransactionType;
import com.triquang.binance.model.Wallet;
import com.triquang.binance.model.WalletTransaction;

public interface WalletTransactionService {
	WalletTransaction createTransaction(Wallet wallet, WalletTransactionType type, String transferId, String purpose,
			Long amount);

	List<WalletTransaction> getTransactions(Wallet wallet, WalletTransactionType type);

}
