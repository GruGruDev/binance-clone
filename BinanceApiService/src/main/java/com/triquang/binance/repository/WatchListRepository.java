package com.triquang.binance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.binance.model.Watchlist;

public interface WatchListRepository extends JpaRepository<Watchlist, Long> {
	Watchlist findByUserId(Long userId);
}
