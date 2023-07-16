package com.pc.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.pc.electronic.store.dtos.PageableResponse;
import com.pc.electronic.store.dtos.ProductDto;
import com.pc.electronic.store.entities.Category;
import com.pc.electronic.store.entities.Product;
import com.pc.electronic.store.exceptions.ResourceNotFoundException;
import com.pc.electronic.store.helper.Helper;
import com.pc.electronic.store.repositories.CategoryRepository;
import com.pc.electronic.store.repositories.ProductRepository;
import com.pc.electronic.store.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	private ProductRepository productRepo;
	
	@Autowired
	private CategoryRepository categoryRepo;
	
	@Autowired
	private ModelMapper mapper;
	
	@Value("${product.image.path}")
	private String imagePath;

	@Override
	public ProductDto create(ProductDto productDto) {
		// TODO Auto-generated method stub
		String productId=UUID.randomUUID().toString();
		productDto.setProductId(productId);
		productDto.setAddedDate(new Date());
		Product product=mapper.map(productDto, Product.class);
		Product savedProduct=productRepo.save(product);
		return mapper.map(savedProduct, ProductDto.class);
	}

	@Override
	public ProductDto update(ProductDto productDto, String productId) {
		// TODO Auto-generated method stub
		Product product=productRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException("No Product found !!"));
		product.setTitle(productDto.getTitle());
		product.setDescription(productDto.getDescription());
		product.setPrice(productDto.getPrice());
		product.setDiscountedPrice(productDto.getDiscountedPrice());
		product.setQuantity(productDto.getQuantity());
		product.setLive(productDto.isLive());
		product.setStock(productDto.isStock());
		product.setImageName(productDto.getImageName());
		Product updatedProduct=productRepo.save(product);
		return mapper.map(updatedProduct, ProductDto.class);
	}

	@Override
	public void delete(String productId) {
		// TODO Auto-generated method stub
		Product product=productRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException("No Product found !!"));
		String fullPath=imagePath+ product.getImageName();
		try {
			Path path=Paths.get(fullPath);
			Files.delete(path);
		} catch(NoSuchFileException ex) {
			ex.printStackTrace();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		productRepo.delete(product);
	}

	@Override
	public ProductDto get(String productId) {
		// TODO Auto-generated method stub
		Product product=productRepo.findById(productId).orElseThrow(()->new ResourceNotFoundException("No Product found !!"));
		return mapper.map(product, ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page=productRepo.findAll(pageable);
		return Helper.getPageableResponse(page,ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page=productRepo.findByLiveTrue(pageable);
		return Helper.getPageableResponse(page,ProductDto.class);
	}

	@Override
	public PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page=productRepo.findByTitleContaining(subTitle,pageable);
		return Helper.getPageableResponse(page,ProductDto.class);
	}
	
	@Override
	public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
		Category category=categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("No such Category found"));
		Product product=mapper.map(productDto, Product.class);
		String productId=UUID.randomUUID().toString();
		product.setProductId(productId);
		product.setAddedDate(new Date());
		product.setCategory(category);
		Product savedProduct=productRepo.save(product);
		return mapper.map(savedProduct, ProductDto.class);
	}
	
	@Override
	public ProductDto updateCategory(String productId, String categoryId) {
		Product product=productRepo.findById(productId).orElseThrow(()-> new ResourceNotFoundException("No such product found"));
		Category category=categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("No such category found"));
		product.setCategory(category);
		Product savedProduct=productRepo.save(product);
		return mapper.map(savedProduct, ProductDto.class);
	}
	
	@Override
	public PageableResponse<ProductDto> getAllOfCategory(String categoryId,int pageNumber, int pageSize, String sortBy, String sortDir){
		Category category=categoryRepo.findById(categoryId).orElseThrow(()-> new ResourceNotFoundException("No such category found"));
		Sort sort=(sortDir.equalsIgnoreCase("desc"))?(Sort.by(sortBy).descending()):(Sort.by(sortBy).ascending());
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<Product> page=productRepo.findByCategory(category,pageable);
		return Helper.getPageableResponse(page, ProductDto.class);
	}

}
