package com.triquang.binance.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

import com.triquang.binance.domain.WithDrawalStatus;

@Entity
@Data
public class Withdrawal {
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Id
	private Long id;

	private WithDrawalStatus status;

	private Long amount;

	@ManyToOne
	private User user;

	private LocalDateTime date;
}
