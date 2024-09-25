package com.triquang.binance.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.triquang.binance.model.Coin;
import com.triquang.binance.model.Order;
import com.triquang.binance.model.User;
import com.triquang.binance.request.CreateOrderRequest;
import com.triquang.binance.service.CoinService;
import com.triquang.binance.service.OrderService;
import com.triquang.binance.service.UserService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
	private OrderService orderService;

	private UserService userSerivce;

	@Autowired
	private CoinService coinService;

	@Autowired
	public OrderController(OrderService orderService, UserService userSerivce) {
		this.orderService = orderService;
		this.userSerivce = userSerivce;
	}

	@PostMapping("/pay")
	public ResponseEntity<Order> payOrderPayment(@RequestHeader("Authorization") String jwt,
			@RequestBody CreateOrderRequest req

	) throws Exception {
		User user = userSerivce.findUserProfileByJwt(jwt);
		Coin coin = coinService.findById(req.getCoinId());

		Order order = orderService.processOrder(coin, req.getQuantity(), req.getOrderType(), user);

		return ResponseEntity.ok(order);

	}

	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(@RequestHeader("Authorization") String jwtToken,
			@PathVariable Long orderId) throws Exception {
		if (jwtToken == null) {
			throw new Exception("Token missing...");
		}

		User user = userSerivce.findUserProfileByJwt(jwtToken);

		Order order = orderService.getOrderById(orderId);
		if (order.getUser().getId().equals(user.getId())) {
			return ResponseEntity.ok(order);
		} else {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
		}
	}

	@GetMapping()
	public ResponseEntity<List<Order>> getAllOrdersForUser(@RequestHeader("Authorization") String jwtToken,
			@RequestParam(required = false) String order_type, @RequestParam(required = false) String asset_symbol)
			throws Exception {
		if (jwtToken == null) {
			throw new Exception("Token missing...");
		}

		Long userId = userSerivce.findUserProfileByJwt(jwtToken).getId();

		List<Order> userOrders = orderService.getAllOrdersForUser(userId, order_type, asset_symbol);
		return ResponseEntity.ok(userOrders);
	}

}
