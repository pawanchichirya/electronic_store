package com.pc.electronic.store.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pc.electronic.store.entities.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

	Page<Product> findByTitleContaining(String subTitle, Pageable pageable);
	
	Page<Product> findByLiveTrue(Pageable pageable);
}
