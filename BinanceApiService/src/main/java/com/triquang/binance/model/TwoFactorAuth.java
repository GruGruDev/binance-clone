package com.triquang.binance.model;

import com.triquang.binance.domain.VerificationType;

import lombok.Data;

@Data
public class TwoFactorAuth {

    private boolean isEnabled = false;
    private VerificationType sendTo;
}
