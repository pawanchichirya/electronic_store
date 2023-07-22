package com.pc.electronic.store.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pc.electronic.store.entities.Cart;
import com.pc.electronic.store.entities.User;

public interface CartRepository extends JpaRepository<Cart,String>{

	Optional<Cart> findByUser(User user);
}
