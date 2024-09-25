package com.triquang.binance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.triquang.binance.domain.USER_ROLE;
import com.triquang.binance.model.User;
import com.triquang.binance.repository.UserRepository;
import com.triquang.binance.utils.OtpUtils;

@Component
public class DataInitializationComponent implements CommandLineRunner {

	private final UserRepository userRepository;

	private PasswordEncoder passwordEncoder;

	@Autowired
	public DataInitializationComponent(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;

	}

	@Override
	public void run(String... args) {
		initializeAdminUser();
	}

	private void initializeAdminUser() {
		String adminUsername = "admin@gmail.com";

		if (userRepository.findByEmail(adminUsername) == null) {
			User adminUser = new User();

			adminUser.setPassword(passwordEncoder.encode("12345678"));
			adminUser.setFullName("Supper Admin");
			adminUser.setEmail(adminUsername);
			adminUser.setRole(USER_ROLE.ROLE_ADMIN);
			adminUser.setAddress("364 Cong Hoa Street, Tan Binh District");
			adminUser.setIdCard(OtpUtils.generateIdCard());
			adminUser.setCountry("VietNam");
			adminUser.setPostCode("467865");
			adminUser.setCity("Ho Chi Minh");
			adminUser.setMobile("0123456789");
			userRepository.save(adminUser);
		}
	}

}
