package com.pc.electronic.store.services;

import java.util.List;

import com.pc.electronic.store.dtos.CreateOrderRequest;
import com.pc.electronic.store.dtos.OrderDto;
import com.pc.electronic.store.dtos.PageableResponse;

public interface OrderService {

	OrderDto createOrder(CreateOrderRequest orderDto);
	void removeOrder(String orderId);
	List<OrderDto> getOrdersOfUser(String userId);
	PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir);
}
