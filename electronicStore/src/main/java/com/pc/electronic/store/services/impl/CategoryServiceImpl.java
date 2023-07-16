package com.pc.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pc.electronic.store.dtos.CategoryDto;
import com.pc.electronic.store.dtos.PageableResponse;
import com.pc.electronic.store.entities.Category;
import com.pc.electronic.store.exceptions.ResourceNotFoundException;
import com.pc.electronic.store.helper.Helper;
import com.pc.electronic.store.repositories.CategoryRepository;
import com.pc.electronic.store.services.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Value("${category.profile.image.path}")
	private String imagePath;

	@Override
	public CategoryDto createCategory(CategoryDto categoryDto) {
		// TODO Auto-generated method stub
		String categoryId=UUID.randomUUID().toString();
		categoryDto.setCategoryId(categoryId);
		Category category=mapper.map(categoryDto, Category.class);
		Category savedCategory=categoryRepository.save(category);
		return mapper.map(savedCategory, CategoryDto.class);
				
	}

	@Override
	public CategoryDto updateCategory(String categoryId, CategoryDto categoryDto) {
		// TODO Auto-generated method stub
		Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("No Category found"));
		category.setTitle(categoryDto.getTitle());
		category.setDescription(categoryDto.getDescription());
		category.setCoverImage(categoryDto.getCoverImage());
		Category updatedCategory=categoryRepository.save(category);
		return mapper.map(updatedCategory, CategoryDto.class);
	}

	@Override
	public void deleteCategory(String categoryId) {
		// TODO Auto-generated method stub
		Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("No Category found"));
		String fullPath=imagePath+ category.getCoverImage();
		try {
			Path path=Paths.get(fullPath);
			Files.delete(path);
		} catch(NoSuchFileException ex) {
			ex.printStackTrace();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		categoryRepository.delete(category);
	}

	@Override
	public PageableResponse<CategoryDto> getAllCategories(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Category> page=categoryRepository.findAll(pageable);
		PageableResponse<CategoryDto> pageableResponse=Helper.getPageableResponse(page,CategoryDto.class);
		return pageableResponse;
	}

	@Override
	public CategoryDto getCategory(String categoryId) {
		// TODO Auto-generated method stub
		Category category=categoryRepository.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("No Category found"));
		return mapper.map(category, CategoryDto.class);
	}

}
