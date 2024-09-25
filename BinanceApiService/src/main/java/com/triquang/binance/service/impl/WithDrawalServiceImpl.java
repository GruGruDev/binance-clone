package com.triquang.binance.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.triquang.binance.domain.WithDrawalStatus;
import com.triquang.binance.model.User;
import com.triquang.binance.model.Withdrawal;
import com.triquang.binance.repository.WithDrawalRepository;
import com.triquang.binance.service.WithDrawalService;

@Service
public class WithDrawalServiceImpl implements WithDrawalService {
	@Autowired
	private WithDrawalRepository withdrawalRepository;

	@Override
	public Withdrawal requestWithdrawal(Long amount, User user) {
		Withdrawal withdrawal = new Withdrawal();
		withdrawal.setAmount(amount);
		withdrawal.setStatus(WithDrawalStatus.PENDING);
		withdrawal.setDate(LocalDateTime.now());
		withdrawal.setUser(user);
		return withdrawalRepository.save(withdrawal);
	}

	@Override
	public Withdrawal procedWithdrawal(Long withdrawalId, boolean accept) throws Exception {
		Optional<Withdrawal> withdrawalOptional = withdrawalRepository.findById(withdrawalId);

		if (withdrawalOptional.isEmpty()) {
			throw new Exception("withdrawal id is wrong...");
		}

		Withdrawal withdrawal = withdrawalOptional.get();

		withdrawal.setDate(LocalDateTime.now());

		if (accept) {
			withdrawal.setStatus(WithDrawalStatus.SUCCESS);
		} else {
			withdrawal.setStatus(WithDrawalStatus.DECLINE);
		}

		return withdrawalRepository.save(withdrawal);
	}

	@Override
	public List<Withdrawal> getUsersWithdrawalHistory(User user) {
		return withdrawalRepository.findByUserId(user.getId());
	}

	@Override
	public List<Withdrawal> getAllWithdrawalRequest() {
		return withdrawalRepository.findAll();
	}

}
