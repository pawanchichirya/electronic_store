package com.pc.electronic.store.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pc.electronic.store.entities.Order;
import com.pc.electronic.store.entities.User;

public interface OrderRepository extends JpaRepository<Order, String>{

	List<Order> findByUser(User user);
}
