package com.pc.electronic.store.services.impl;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pc.electronic.store.dtos.CreateOrderRequest;
import com.pc.electronic.store.dtos.OrderDto;
import com.pc.electronic.store.dtos.PageableResponse;
import com.pc.electronic.store.dtos.ProductDto;
import com.pc.electronic.store.entities.User;
import com.pc.electronic.store.entities.Cart;
import com.pc.electronic.store.entities.CartItem;
import com.pc.electronic.store.entities.Order;
import com.pc.electronic.store.entities.OrderItem;
import com.pc.electronic.store.entities.Product;
import com.pc.electronic.store.exceptions.BadApiRequest;
import com.pc.electronic.store.exceptions.ResourceNotFoundException;
import com.pc.electronic.store.helper.Helper;
import com.pc.electronic.store.repositories.CartRepository;
import com.pc.electronic.store.repositories.OrderRepository;
import com.pc.electronic.store.repositories.UserRepository;
import com.pc.electronic.store.services.OrderService;

@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private OrderRepository orderRepo;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private CartRepository cartRepo;

	@Override
	public OrderDto createOrder(CreateOrderRequest orderDto) {
		// TODO Auto-generated method stub
		String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));
        Cart cart = cartRepo.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart with given id not found on server !!"));

        List<CartItem> cartItems = cart.getItems();

        if (cartItems.size() <= 0) {
            throw new BadApiRequest("Invalid number of items in cart !!");

        }

        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhone(orderDto.getBillingPhone())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(null)
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build();
        
        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = cartItems.stream().map(cartItem -> {
            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .product(cartItem.getProduct())
                    .totalPrice(cartItem.getQuantity() * cartItem.getProduct().getDiscountedPrice())
                    .order(order)
                    .build();

            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());

        order.setOrderItems(orderItems);
        order.setOrderAmount(orderAmount.get());

        //
        cart.getItems().clear();
        cartRepo.save(cart);
        Order savedOrder = orderRepo.save(order);
        return modelMapper.map(savedOrder, OrderDto.class);
	}

	@Override
	public void removeOrder(String orderId) {
		// TODO Auto-generated method stub
		Order order=orderRepo.findById(orderId).orElseThrow(()-> new ResourceNotFoundException("order not found !!"));
		orderRepo.delete(order);
	}

	@Override
	public List<OrderDto> getOrdersOfUser(String userId) {
		// TODO Auto-generated method stub
		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found !!"));
        List<Order> orders = orderRepo.findByUser(user);
        List<OrderDto> orderDtos = orders.stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList());
        return orderDtos;
	}

	@Override
	public PageableResponse<OrderDto> getOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Order> page=orderRepo.findAll(pageable);
		return Helper.getPageableResponse(page,OrderDto.class);
	}

}
