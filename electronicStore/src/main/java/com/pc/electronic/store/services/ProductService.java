package com.pc.electronic.store.services;

import java.util.List;

import com.pc.electronic.store.dtos.PageableResponse;
import com.pc.electronic.store.dtos.ProductDto;

public interface ProductService {

	ProductDto create(ProductDto productDto);
	ProductDto update(ProductDto productDto, String productId);
	void delete(String productId);
	ProductDto get(String productId);
	PageableResponse<ProductDto> getAll(int pageNumber, int pageSize, String sortBy, String sortDir);
	PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);
	PageableResponse<ProductDto> searchByTitle(String subTitle, int pageNumber, int pageSize, String sortBy, String sortDir);
}
