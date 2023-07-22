package com.pc.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pc.electronic.store.entities.CartItem;

public interface CartItemRepository extends JpaRepository<CartItem,Integer>{

}
