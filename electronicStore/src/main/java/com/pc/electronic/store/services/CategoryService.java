package com.pc.electronic.store.services;

import java.util.List;

import com.pc.electronic.store.dtos.CategoryDto;
import com.pc.electronic.store.dtos.PageableResponse;

public interface CategoryService {
	
	//create
	public CategoryDto createCategory(CategoryDto categoryDto);
	
	//update
	public CategoryDto updateCategory(String categoryId,CategoryDto categoryDto);
	
	//delete
	public void deleteCategory(String categoryId);
	
	//get all
	public PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir); 
	
	//get single category
	public CategoryDto getCategory(String categoryId);

}
