package com.triquang.binance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.triquang.binance.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
}
