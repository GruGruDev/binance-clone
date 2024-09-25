package com.triquang.binance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.binance.model.Withdrawal;

public interface WithDrawalRepository extends JpaRepository<Withdrawal, Long> {
	List<Withdrawal> findByUserId(Long userId);
	
}
