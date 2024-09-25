package com.triquang.binance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.binance.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
