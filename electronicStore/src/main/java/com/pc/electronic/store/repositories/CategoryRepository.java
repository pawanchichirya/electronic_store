package com.pc.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pc.electronic.store.entities.Category;

public interface CategoryRepository extends JpaRepository<Category,String>{

}
