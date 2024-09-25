package com.triquang.binance.service;

import com.triquang.binance.exception.WalletException;
import com.triquang.binance.model.Order;
import com.triquang.binance.model.User;
import com.triquang.binance.model.Wallet;

public interface WalletService {
	Wallet getUserWallet(User user) throws WalletException;

	public Wallet addBalanceToWallet(Wallet wallet, Long money) throws WalletException;

	public Wallet findWalletById(Long id) throws WalletException;

	public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws WalletException;

	public Wallet payOrderPayment(Order order, User user) throws WalletException;
}
