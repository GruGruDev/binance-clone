package com.triquang.binance.security;

import java.security.SecureRandom;
import java.util.Base64;

public class JwtConstans {
	public static final String SECRET_KEY = "llu3nnpIQPsh1OfWi743cX6OZOogaX+SvESey4GODi0qx1KoygVqU8Bb42xNPgigFEuCc4emTYegK4rPDynA9Q==";
	public static final String JWT_HEADER = "Authorization";

	public static void main(String[] args) {
		int keyLength = 64; // Length of the secret key in bytes

		// Generate random bytes
		byte[] keyBytes = new byte[keyLength];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(keyBytes);

		// Encode the random bytes to Base64
		String secretKey = Base64.getEncoder().encodeToString(keyBytes);

		System.out.println("Random Secret Key: " + secretKey);
	}

}
