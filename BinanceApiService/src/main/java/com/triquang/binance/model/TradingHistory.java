package com.triquang.binance.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradingHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private double sellingPrice;

    private double buyingPrice;

    @Embedded
    private Coin coin;

    @ManyToOne
    private User user;
}
