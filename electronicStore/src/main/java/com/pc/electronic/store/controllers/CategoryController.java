package com.pc.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pc.electronic.store.dtos.ApiResponseMessage;
import com.pc.electronic.store.dtos.CategoryDto;
import com.pc.electronic.store.dtos.ImageResponse;
import com.pc.electronic.store.dtos.PageableResponse;
import com.pc.electronic.store.dtos.UserDto;
import com.pc.electronic.store.services.CategoryService;
import com.pc.electronic.store.services.FileService;

@RestController
@RequestMapping("/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${category.profile.image.path}")
	private String imageUploadPath;
	
	
	
	@PostMapping
	public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto){
		CategoryDto categoryDto1=categoryService.createCategory(categoryDto);
		return new ResponseEntity<>(categoryDto1, HttpStatus.CREATED);	
	}
	
	@PutMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> updateCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable("categoryId") String categoryId){
		CategoryDto updatedCategoryDto=categoryService.updateCategory(categoryId, categoryDto);
		return new ResponseEntity<>(updatedCategoryDto, HttpStatus.OK);
	}
	
	@GetMapping("/{categoryId}")
	public ResponseEntity<CategoryDto> getCategory(@PathVariable("categoryId") String categoryId){
		CategoryDto categoryDto1=categoryService.getCategory(categoryId);
		return new ResponseEntity<>(categoryDto1, HttpStatus.OK);
	}
	
	@DeleteMapping("/{categoryId}")
	public ResponseEntity<ApiResponseMessage> deleteCategory(@PathVariable("categoryId") String categoryId){
		categoryService.deleteCategory(categoryId);
		ApiResponseMessage apiResponseMessage=ApiResponseMessage.builder().message("Category deleted successfully").status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<CategoryDto>> getAllCategories(
			@RequestParam(value="pageNumber", defaultValue="0", required=false) int pageNumber,
			@RequestParam(value="pageSize", defaultValue="2", required=false) int pageSize,
			@RequestParam(value="sortBy", defaultValue="title", required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue="asc", required=false) String sortDir){
		PageableResponse<CategoryDto> pageableResponse=categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}
	
	//upload categry image
	@PostMapping("/image/{categoryId}")
	public ResponseEntity<ImageResponse> uploadCategoryImage(@RequestParam("coverImage") MultipartFile image, @PathVariable("categoryId") String categoryId) throws IOException{
		String imageName=fileService.uploadFile(image, imageUploadPath);
		CategoryDto category=categoryService.getCategory(categoryId);
		category.setCoverImage(imageName);
		CategoryDto categoryDto=categoryService.updateCategory(categoryId, category);
		ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
		return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
	}
	
	@GetMapping("/image/{categoryId}")
	public void serveCategoryImage(@PathVariable String categoryId, HttpServletResponse response) throws IOException {
		CategoryDto category=categoryService.getCategory(categoryId);
		InputStream resource=fileService.getResource(imageUploadPath, category.getCoverImage());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource,(response.getOutputStream()));
	}
}
