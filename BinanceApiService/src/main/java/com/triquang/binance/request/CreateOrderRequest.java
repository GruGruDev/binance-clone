package com.triquang.binance.request;

import com.triquang.binance.domain.OrderType;

import lombok.Data;

@Data
public class CreateOrderRequest {
    private String coinId;
    private double quantity;
    private OrderType orderType;
}
