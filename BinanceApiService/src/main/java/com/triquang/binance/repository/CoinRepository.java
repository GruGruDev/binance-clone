package com.triquang.binance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.binance.model.Coin;

public interface CoinRepository extends JpaRepository<Coin, String> {

}
