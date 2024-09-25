package com.triquang.binance.model;

import com.triquang.binance.domain.PaymentMethod;
import com.triquang.binance.domain.PaymentOrderStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PaymentOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Long amount;

	private PaymentOrderStatus status = PaymentOrderStatus.PENDING;

	private PaymentMethod paymentMethod;

	@ManyToOne
	private User user;
}
