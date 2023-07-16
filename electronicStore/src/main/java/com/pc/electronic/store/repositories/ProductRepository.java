package com.pc.electronic.store.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.pc.electronic.store.entities.Category;
import com.pc.electronic.store.entities.Product;

public interface ProductRepository extends JpaRepository<Product, String>{

	Page<Product> findByTitleContaining(String subTitle, Pageable pageable);
	
	Page<Product> findByLiveTrue(Pageable pageable);
	
	Page<Product> findByCategory(Category category, Pageable pageable);
}
