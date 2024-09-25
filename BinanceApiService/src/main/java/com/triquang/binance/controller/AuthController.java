package com.triquang.binance.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.triquang.binance.exception.UserException;
import com.triquang.binance.model.TwoFactorOTP;
import com.triquang.binance.model.User;
import com.triquang.binance.repository.UserRepository;
import com.triquang.binance.request.LoginRequest;
import com.triquang.binance.response.AuthResponse;
import com.triquang.binance.security.JwtProvider;
import com.triquang.binance.service.CustomUserService;
import com.triquang.binance.service.EmailService;
import com.triquang.binance.service.TwoFactorOTPService;
import com.triquang.binance.service.UserService;
import com.triquang.binance.service.WatchListService;
import com.triquang.binance.utils.OtpUtils;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private CustomUserService userDetails;

	@Autowired
	private TwoFactorOTPService factorOTPService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private WatchListService watchListService;

	@Autowired
	private UserService userService;

	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws UserException {

		String email = user.getEmail();
		String password = user.getPassword();
		String fullName = user.getFullName();
		String mobile = user.getMobile();

		User isEmailExist = userRepository.findByEmail(email);
		if (isEmailExist != null) {
			throw new UserException("Email Is already used with another account");
		}

		// Create new user
		User createdUser = new User();
		createdUser.setEmail(email);
		createdUser.setFullName(fullName);
		createdUser.setMobile(mobile);
		createdUser.setPassword(passwordEncoder.encode(password));
		createdUser.setCity(user.getCity());
		createdUser.setPostCode(user.getPostCode());
		createdUser.setIdCard(OtpUtils.generateIdCard());
		createdUser.setCountry(user.getCountry());
		createdUser.setAddress(user.getAddress());
		User savedUser = userRepository.save(createdUser);

		watchListService.createWatchList(savedUser);

		Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = JwtProvider.generateToken(authentication);

		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Register Success");

		return new ResponseEntity<AuthResponse>(authResponse, HttpStatus.OK);

	}

	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> signing(@RequestBody LoginRequest loginRequest)
			throws UserException, MessagingException {

		String username = loginRequest.getEmail();
		String password = loginRequest.getPassword();

		System.out.println(username + " ----- " + password);

		Authentication authentication = authenticate(username, password);

		User user = userService.findUserByEmail(username);

		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = JwtProvider.generateToken(authentication);

		if (user.getTwoFactorAuth().isEnabled()) {
			AuthResponse authResponse = new AuthResponse();
			authResponse.setMessage("Two factor authentication enabled");
			authResponse.setTwoFactorAuthEnabled(true);

			String otp = OtpUtils.generateOTP();

			TwoFactorOTP oldTwoFactorOTP = factorOTPService.findByUser(user.getId());
			if (oldTwoFactorOTP != null) {
				factorOTPService.deleteTwoFactorOtp(oldTwoFactorOTP);
			}

			TwoFactorOTP twoFactorOTP = factorOTPService.createTwoFactorOtp(user, otp, token);

			emailService.sendVerificationOtpEmail(user.getEmail(), otp);

			authResponse.setSession(twoFactorOTP.getId());
			return new ResponseEntity<>(authResponse, HttpStatus.OK);
		}

		AuthResponse authResponse = new AuthResponse();

		authResponse.setMessage("Login Success");
		authResponse.setJwt(token);

		return new ResponseEntity<>(authResponse, HttpStatus.OK);
	}

	@GetMapping("/login/google")
	public void redirectToGoogle(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// Redirect to the Google OAuth2 authorization URI
		response.sendRedirect("/login/oauth2/authorization/google");
	}

	@GetMapping("/login/oauth2/code/google")
	public User handleGoogleCallback(@RequestParam(required = false, name = "code") String code,
			@RequestParam(required = false, name = "state") String state, OAuth2AuthenticationToken authentication) {

		// Extract user details from the authentication object or access token
		String email = authentication.getPrincipal().getAttribute("email");
		String fullName = authentication.getPrincipal().getAttribute("name");
		// You can extract more details as needed

		User user = new User();
		user.setEmail(email);
		user.setFullName(fullName);

		return user;
	}

	@PostMapping("/two-factor/otp/{otp}")
	public ResponseEntity<AuthResponse> verifySigningOtp(@PathVariable String otp, @RequestParam String id)
			throws Exception {

		TwoFactorOTP twoFactorOTP = factorOTPService.findById(id);

		if (factorOTPService.verifyTwoFactorOtp(twoFactorOTP, otp)) {
			AuthResponse authResponse = new AuthResponse();
			authResponse.setMessage("Two factor authentication verified");
			authResponse.setTwoFactorAuthEnabled(true);
			authResponse.setJwt(twoFactorOTP.getJwt());
			return new ResponseEntity<>(authResponse, HttpStatus.OK);
		}
		throw new Exception("Invalid OTP");
	}

	private Authentication authenticate(String username, String password) {
		UserDetails details = userDetails.loadUserByUsername(username);
		if (details == null || !passwordEncoder.matches(password, details.getPassword())) {
			throw new BadCredentialsException("Invalid username or password");
		}
		return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
	}
}
