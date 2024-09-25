package com.triquang.binance.service;

import java.util.List;

import com.triquang.binance.model.User;
import com.triquang.binance.model.Withdrawal;

public interface WithDrawalService {
	Withdrawal requestWithdrawal(Long amount, User user);

	Withdrawal procedWithdrawal(Long withdrawalId, boolean accept) throws Exception;

	List<Withdrawal> getUsersWithdrawalHistory(User user);

	List<Withdrawal> getAllWithdrawalRequest();
}
