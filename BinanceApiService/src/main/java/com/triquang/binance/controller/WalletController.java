package com.triquang.binance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.triquang.binance.domain.WalletTransactionType;
import com.triquang.binance.model.Order;
import com.triquang.binance.model.PaymentOrder;
import com.triquang.binance.model.User;
import com.triquang.binance.model.Wallet;
import com.triquang.binance.model.WalletTransaction;
import com.triquang.binance.response.PaymentResponse;
import com.triquang.binance.service.OrderService;
import com.triquang.binance.service.PaymentService;
import com.triquang.binance.service.UserService;
import com.triquang.binance.service.WalletService;
import com.triquang.binance.service.WalletTransactionService;

@RestController
public class WalletController {
	@Autowired
	private WalletService walleteService;

	@Autowired
	private UserService userService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private WalletTransactionService walletTransactionService;

	@Autowired
	private PaymentService paymentService;

	@GetMapping("/api/wallet")
	public ResponseEntity<?> getUserWallet(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);

		Wallet wallet = walleteService.getUserWallet(user);

		return new ResponseEntity<>(wallet, HttpStatus.OK);
	}

	@GetMapping("/api/wallet/transactions")
	public ResponseEntity<List<WalletTransaction>> getWalletTransaction(@RequestHeader("Authorization") String jwt)
			throws Exception {
		User user = userService.findUserProfileByJwt(jwt);

		Wallet wallet = walleteService.getUserWallet(user);

		List<WalletTransaction> transactions = walletTransactionService.getTransactions(wallet, null);

		return new ResponseEntity<>(transactions, HttpStatus.OK);
	}

	@PutMapping("/api/wallet/deposit/amount/{amount}")
	public ResponseEntity<PaymentResponse> depositMoney(@RequestHeader("Authorization") String jwt,
			@PathVariable Long amount) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Wallet wallet = walleteService.getUserWallet(user);
		PaymentResponse res = new PaymentResponse();
		res.setPayment_url("deposite success");
		walleteService.addBalanceToWallet(wallet, amount);

		return new ResponseEntity<>(res, HttpStatus.OK);

	}

	@PutMapping("/api/wallet/deposit")
	public ResponseEntity<Wallet> addMoneyToWallet(@RequestHeader("Authorization") String jwt,
			@RequestParam(name = "order_id") Long orderId, @RequestParam(name = "payment_id") String paymentId)
			throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Wallet wallet = walleteService.getUserWallet(user);

		PaymentOrder order = paymentService.getPaymentOrderById(orderId);
		Boolean status = paymentService.proccedPaymentOrder(order, paymentId);
		PaymentResponse res = new PaymentResponse();
		res.setPayment_url("deposite success");

		if (status) {
			wallet = walleteService.addBalanceToWallet(wallet, order.getAmount());
		}

		return new ResponseEntity<>(wallet, HttpStatus.OK);

	}

	@PutMapping("/api/wallet/{walletId}/transfer")
	public ResponseEntity<Wallet> walletToWalletTransfer(@RequestHeader("Authorization") String jwt,
			@PathVariable Long walletId, @RequestBody WalletTransaction req) throws Exception {
		User senderUser = userService.findUserProfileByJwt(jwt);

		Wallet reciverWallet = walleteService.findWalletById(walletId);

		Wallet wallet = walleteService.walletToWalletTransfer(senderUser, reciverWallet, req.getAmount());
		walletTransactionService.createTransaction(wallet, WalletTransactionType.WALLET_TRANSFER,
				reciverWallet.getId().toString(), req.getPurpose(), -req.getAmount());

		return new ResponseEntity<>(wallet, HttpStatus.OK);

	}

	@PutMapping("/api/wallet/order/{orderId}/pay")
	public ResponseEntity<Wallet> payOrderPayment(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Order order = orderService.getOrderById(orderId);

		Wallet wallet = walleteService.payOrderPayment(order, user);

		return new ResponseEntity<>(wallet, HttpStatus.OK);

	}

}
