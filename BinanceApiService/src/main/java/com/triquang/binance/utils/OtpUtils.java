package com.triquang.binance.utils;

import java.util.Random;

public class OtpUtils {
	public static String generateOTP() {
		int otpLength = 6;
		Random random = new Random();
		StringBuilder builder = new StringBuilder(otpLength);
		for (int i = 0; i < otpLength; i++) {
			builder.append(random.nextInt(10));
		}
		return builder.toString();
	}
	
	public static String generateIdCard() {
		int otpLength = 10;
		Random random = new Random();
		StringBuilder builder = new StringBuilder(otpLength);
		for (int i = 0; i < otpLength; i++) {
			builder.append(random.nextInt(10));
		}
		return builder.toString();
	}
}
