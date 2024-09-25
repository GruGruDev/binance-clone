package com.triquang.binance.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triquang.binance.domain.OrderType;
import com.triquang.binance.exception.WalletException;
import com.triquang.binance.model.Order;
import com.triquang.binance.model.User;
import com.triquang.binance.model.Wallet;
import com.triquang.binance.model.WalletTransaction;
import com.triquang.binance.repository.WalletRepository;
import com.triquang.binance.repository.WalletTransactionRepository;
import com.triquang.binance.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {

	@Autowired
	private WalletRepository walletRepository;

	@Autowired
	private WalletTransactionRepository walletTransactionRepository;

	public Wallet genrateWallete(User user) {
		Wallet wallet = new Wallet();
		wallet.setUser(user);
		return walletRepository.save(wallet);
	}

	@Override
	public Wallet getUserWallet(User user) throws WalletException {

		Wallet wallet = walletRepository.findByUserId(user.getId());
		if (wallet != null) {
			return wallet;
		}

		wallet = genrateWallete(user);
		return wallet;
	}

	@Override
	public Wallet findWalletById(Long id) throws WalletException {
		Optional<Wallet> wallet = walletRepository.findById(id);
		if (wallet.isPresent()) {
			return wallet.get();
		}
		throw new WalletException("Wallet not found with id " + id);
	}

	@Override
	public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) throws WalletException {
		Wallet senderWallet = getUserWallet(sender);

		if (senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount)) < 0) {
			throw new WalletException("Insufficient balance...");
		}

		BigDecimal senderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
		senderWallet.setBalance(senderBalance);
		walletRepository.save(senderWallet);

		BigDecimal receiverBalance = receiverWallet.getBalance();
		receiverBalance = receiverBalance.add(BigDecimal.valueOf(amount));
		receiverWallet.setBalance(receiverBalance);
		walletRepository.save(receiverWallet);

		return senderWallet;
	}

	@Override
	public Wallet payOrderPayment(Order order, User user) throws WalletException {
		Wallet wallet = getUserWallet(user);

		WalletTransaction walletTransaction = new WalletTransaction();
		walletTransaction.setWallet(wallet);
		walletTransaction.setPurpose(order.getOrderType() + " " + order.getOrderItem().getCoin().getId());

		walletTransaction.setDate(LocalDate.now());
		walletTransaction.setTransferId(order.getOrderItem().getCoin().getSymbol());

		if (order.getOrderType().equals(OrderType.BUY)) {
			walletTransaction.setAmount(-order.getPrice().longValue());
			BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());

			if (newBalance.compareTo(order.getPrice()) < 0) {
				System.out.println("inside");
				throw new WalletException("Insufficient funds for this transaction.");
			}
			wallet.setBalance(newBalance);
		} else if (order.getOrderType().equals(OrderType.SELL)) {
			walletTransaction.setAmount(order.getPrice().longValue());
			BigDecimal newBalance = wallet.getBalance().add(order.getPrice());
			wallet.setBalance(newBalance);
		}

		walletTransactionRepository.save(walletTransaction);
		walletRepository.save(wallet);
		return wallet;
	}

	@Override
	public Wallet addBalanceToWallet(Wallet wallet, Long money) throws WalletException {

		wallet.getBalance().add(BigDecimal.valueOf(money));

		wallet.setBalance(wallet.getBalance().add(BigDecimal.valueOf(money)));

		walletRepository.save(wallet);
		System.out.println("updated wallet - " + wallet);
		return wallet;
	}

}
