package com.triquang.binance.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.triquang.binance.domain.USER_ROLE;
import com.triquang.binance.domain.UserStatus;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")  
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String fullName;
	private String email;
	private String mobile;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	private UserStatus status= UserStatus.PENDING;

	private boolean isVerified = false;

	@Embedded
	private TwoFactorAuth twoFactorAuth= new TwoFactorAuth();

	private String address;
	private String city;
	private String postCode;
	private String country;
	private String idCard;
	
	private LocalDateTime createdAt = LocalDateTime.now();

	private USER_ROLE role= USER_ROLE.ROLE_USER;

}
