package com.triquang.binance.service;

import com.triquang.binance.model.CoinDTO;
import com.triquang.binance.response.ApiResponse;

public interface ChatBotService {
	ApiResponse getCoinDetails(String coinName);

	CoinDTO getCoinByName(String coinName);

	String simpleChat(String prompt);
}
