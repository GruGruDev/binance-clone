package com.triquang.binance.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String password;
    private String otp;
}
