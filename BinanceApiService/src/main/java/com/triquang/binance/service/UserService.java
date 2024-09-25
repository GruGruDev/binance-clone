package com.triquang.binance.service;

import com.triquang.binance.domain.VerificationType;
import com.triquang.binance.exception.UserException;
import com.triquang.binance.model.User;

public interface UserService {
	public User findUserProfileByJwt(String jwt) throws UserException;

	public User findUserByEmail(String email) throws UserException;

	public User findUserById(Long userId) throws UserException;

	public User verifyUser(User user) throws UserException;

	public User enabledTwoFactorAuthentication(VerificationType verificationType, String sendTo, User user)
			throws UserException;

	User updatePassword(User user, String newPassword);

	void sendUpdatePasswordOtp(String email, String otp);

}
