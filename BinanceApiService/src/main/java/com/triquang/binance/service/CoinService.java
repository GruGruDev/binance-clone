package com.triquang.binance.service;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.triquang.binance.model.Coin;

public interface CoinService {
	List<Coin> getCoinList(int page) throws Exception;

	String getMarketChart(String coinId, int days) throws Exception;

	String getCoinDetails(String coinId) throws JsonProcessingException;

	Coin findById(String coinId) throws Exception;

	String searchCoin(String keyword);

	String getTop50CoinsByMarketCapRank();

	String getTrendingCoins();
}
