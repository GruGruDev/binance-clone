package com.triquang.binance.service;

import java.util.List;

import com.triquang.binance.domain.OrderType;
import com.triquang.binance.model.Coin;
import com.triquang.binance.model.Order;
import com.triquang.binance.model.OrderItem;
import com.triquang.binance.model.User;

public interface OrderService {

	Order createOrder(User user, OrderItem orderItem, OrderType orderType);

	Order getOrderById(Long orderId);

	List<Order> getAllOrdersForUser(Long userId, String orderType, String assetSymbol);

	void cancelOrder(Long orderId);

	Order processOrder(Coin coin, double quantity, OrderType orderType, User user) throws Exception;

}