package com.triquang.binance.response;

import lombok.Data;

@Data
public class FunctionResponse {
    private String functionName;
    private String currencyName;
    private String currencyData;
}
