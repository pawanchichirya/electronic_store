package com.pc.electronic.store.services.impl;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pc.electronic.store.dtos.AddItemToCartRequest;
import com.pc.electronic.store.dtos.CartDto;
import com.pc.electronic.store.entities.Cart;
import com.pc.electronic.store.entities.CartItem;
import com.pc.electronic.store.entities.Product;
import com.pc.electronic.store.entities.User;
import com.pc.electronic.store.exceptions.BadApiRequest;
import com.pc.electronic.store.exceptions.ResourceNotFoundException;
import com.pc.electronic.store.repositories.CartItemRepository;
import com.pc.electronic.store.repositories.CartRepository;
import com.pc.electronic.store.repositories.ProductRepository;
import com.pc.electronic.store.repositories.UserRepository;
import com.pc.electronic.store.services.CartService;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private ProductRepository productRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private ModelMapper mapper;
	@Autowired
	private CartItemRepository cartItemRepo;
	@Autowired
	private CartRepository cartRepo;
	
	@Override
	public CartDto addItemToCart(String userId, AddItemToCartRequest request) {
		// TODO Auto-generated method stub
		int quantity=request.getQuantity();
		String productId=request.getProductId();
		
		if(quantity<=0) {
			throw new BadApiRequest("Requested quantity is not valid !!!");
		}
		
		Product product=productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("No such product exists"));
		User user=userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("No such user exists"));
		
		Cart cart=null;
		try {
			cart=cartRepo.findByUser(user).get();
		} catch(NoSuchElementException e) {
			cart=new Cart();
			cart.setCartId(UUID.randomUUID().toString());
			cart.setCreatedAt(new Date());	
			}
		
		AtomicReference<Boolean> updated=new AtomicReference<>(false);
		List<CartItem> items=cart.getItems();
		items=items.stream().map(item -> {
			if(item.getProduct().getProductId().equals(productId)) {
				item.setQuantity(quantity);
				item.setTotalPrice(quantity*product.getDiscountedPrice());
				updated.set(true);
			}
			return item;
		}).collect(Collectors.toList());
		
		if(!updated.get()) {
			CartItem cartItem=CartItem.builder().quantity(quantity).totalPrice(quantity*product.getDiscountedPrice()).cart(cart).product(product).build();
			cart.getItems().add(cartItem);
			}
		
		cart.setUser(user);
		Cart updatedCart=cartRepo.save(cart);
		return mapper.map(updatedCart,CartDto.class);
	}

	@Override
	public void removeItemFromCart(String userId, int cartItem) {
		// TODO Auto-generated method stub
		CartItem cartItem1=cartItemRepo.findById(cartItem).orElseThrow(()-> new ResourceNotFoundException("No such cart exists"));
		cartItemRepo.delete(cartItem1);
	}

	@Override
	public void clearCart(String userId) {
		// TODO Auto-generated method stub
		User user=userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("No such user exists"));
		Cart cart=cartRepo.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("No such cart exists"));
		cart.getItems().clear();
		cartRepo.save(cart);
	}

	@Override
	public CartDto getCartByUser(String userId) {
		// TODO Auto-generated method stub
		User user=userRepo.findById(userId).orElseThrow(()-> new ResourceNotFoundException("No such user exists"));
		Cart cart=cartRepo.findByUser(user).orElseThrow(()-> new ResourceNotFoundException("No such cart exists"));
		return mapper.map(cart, CartDto.class);
	}

}
