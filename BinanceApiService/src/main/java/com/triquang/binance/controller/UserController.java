package com.triquang.binance.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.triquang.binance.domain.VerificationType;
import com.triquang.binance.exception.UserException;
import com.triquang.binance.model.ForgotPasswordToken;
import com.triquang.binance.model.User;
import com.triquang.binance.model.VerificationCode;
import com.triquang.binance.request.ResetPasswordRequest;
import com.triquang.binance.request.UpdatePasswordRequest;
import com.triquang.binance.response.ApiResponse;
import com.triquang.binance.response.AuthResponse;
import com.triquang.binance.service.EmailService;
import com.triquang.binance.service.ForgotPasswordService;
import com.triquang.binance.service.UserService;
import com.triquang.binance.service.VerificationService;
import com.triquang.binance.utils.OtpUtils;

@RestController
public class UserController {
	@Autowired
	private UserService userService;

	@Autowired
	private VerificationService verificationService;

	@Autowired
	private ForgotPasswordService forgotPasswordService;

	@Autowired
	private EmailService emailService;

	@GetMapping("/api/users/profile")
	public ResponseEntity<User> getUserProfileHandler(@RequestHeader("Authorization") String jwt) throws UserException {

		User user = userService.findUserProfileByJwt(jwt);
		user.setPassword(null);

		return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
	}

	@GetMapping("/api/users/{userId}")
	public ResponseEntity<User> findUserById(@PathVariable Long userId, @RequestHeader("Authorization") String jwt)
			throws UserException {

		User user = userService.findUserById(userId);
		user.setPassword(null);

		return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
	}

	@GetMapping("/api/users/email/{email}")
	public ResponseEntity<User> findUserByEmail(@PathVariable String email, @RequestHeader("Authorization") String jwt)
			throws UserException {

		User user = userService.findUserByEmail(email);

		return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
	}

	@PatchMapping("/api/users/enable-two-factor/verify-otp/{otp}")
	public ResponseEntity<User> enabledTwoFactorAuthentication(@RequestHeader("Authorization") String jwt,
			@PathVariable String otp) throws Exception {

		User user = userService.findUserProfileByJwt(jwt);

		VerificationCode verificationCode = verificationService.findUsersVerification(user);

		String sendTo = verificationCode.getVerificationType().equals(VerificationType.EMAIL)
				? verificationCode.getEmail()
				: verificationCode.getMobile();

		boolean isVerified = verificationService.VerifyOtp(otp, verificationCode);

		if (isVerified) {
			User updatedUser = userService.enabledTwoFactorAuthentication(verificationCode.getVerificationType(),
					sendTo, user);
			verificationService.deleteVerification(verificationCode);
			return ResponseEntity.ok(updatedUser);
		}
		throw new Exception("wrong otp");

	}

	@PatchMapping("/auth/users/reset-password/verify-otp")
	public ResponseEntity<ApiResponse> resetPassword(@RequestParam String id, @RequestBody ResetPasswordRequest req)
			throws Exception {
		ForgotPasswordToken forgotPasswordToken = forgotPasswordService.findById(id);

		boolean isVerified = forgotPasswordService.verifyToken(forgotPasswordToken, req.getOtp());

		if (isVerified) {

			userService.updatePassword(forgotPasswordToken.getUser(), req.getPassword());
			ApiResponse apiResponse = new ApiResponse();
			apiResponse.setMessage("password updated successfully");
			return ResponseEntity.ok(apiResponse);
		}
		throw new Exception("wrong otp");

	}

	@PostMapping("/auth/users/reset-password/send-otp")
	public ResponseEntity<AuthResponse> sendUpdatePasswordOTP(@RequestBody UpdatePasswordRequest req) throws Exception {

		User user = userService.findUserByEmail(req.getSendTo());
		String otp = OtpUtils.generateOTP();
		UUID uuid = UUID.randomUUID();
		String id = uuid.toString();

		ForgotPasswordToken token = forgotPasswordService.findByUser(user.getId());

		if (token == null) {
			token = forgotPasswordService.createToken(user, id, otp, req.getVerificationType(), req.getSendTo());
		}

		if (req.getVerificationType().equals(VerificationType.EMAIL)) {
			emailService.sendVerificationOtpEmail(user.getEmail(), token.getOtp());
		}

		AuthResponse res = new AuthResponse();
		res.setSession(token.getId());
		res.setMessage("Password reset OTP sent successfully.");

		return ResponseEntity.ok(res);

	}

	@PatchMapping("/api/users/verification/verify-otp/{otp}")
	public ResponseEntity<User> verifyOTP(@RequestHeader("Authorization") String jwt, @PathVariable String otp)
			throws Exception {

		User user = userService.findUserProfileByJwt(jwt);

		VerificationCode verificationCode = verificationService.findUsersVerification(user);
		boolean isVerified = verificationService.VerifyOtp(otp, verificationCode);

		if (isVerified) {
			verificationService.deleteVerification(verificationCode);
			User verifiedUser = userService.verifyUser(user);
			return ResponseEntity.ok(verifiedUser);
		}
		throw new Exception("Wrong OTP");

	}

	@PostMapping("/api/users/verification/{verificationType}/send-otp")
	public ResponseEntity<String> sendVerificationOTP(@PathVariable VerificationType verificationType,
			@RequestHeader("Authorization") String jwt) throws Exception {

		User user = userService.findUserProfileByJwt(jwt);

		VerificationCode verificationCode = verificationService.findUsersVerification(user);

		if (verificationCode == null) {
			verificationCode = verificationService.sendVerificationOTP(user, verificationType);
		}

		if (verificationType.equals(VerificationType.EMAIL)) {
			emailService.sendVerificationOtpEmail(user.getEmail(), verificationCode.getOtp());
		}

		return ResponseEntity.ok("Verification OTP sent successfully.");

	}
}