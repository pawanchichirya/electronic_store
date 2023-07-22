package com.pc.electronic.store.services;

import com.pc.electronic.store.dtos.AddItemToCartRequest;
import com.pc.electronic.store.dtos.CartDto;

public interface CartService {

	CartDto addItemToCart(String userId, AddItemToCartRequest request);
	void removeItemFromCart(String userId, int cartItem);
	void clearCart(String userId);
	CartDto getCartByUser(String userId);
	
}
