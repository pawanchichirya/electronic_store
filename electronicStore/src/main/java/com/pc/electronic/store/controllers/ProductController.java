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
import com.pc.electronic.store.dtos.ProductDto;
import com.pc.electronic.store.services.FileService;
import com.pc.electronic.store.services.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

	@Autowired
	private ProductService productService;
	
	@Autowired
	private FileService fileService;
	
	@Value("${product.image.path}")
	private String imageUploadPath;
	
	
	@PostMapping
	public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto){
		ProductDto productDto1=productService.create(productDto);
		return new ResponseEntity<>(productDto1, HttpStatus.CREATED);	
	}
	
	@PutMapping("/{productId}")
	public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto, @PathVariable("productId") String productId){
		ProductDto updatedProductDto=productService.update(productDto, productId);
		return new ResponseEntity<>(updatedProductDto, HttpStatus.OK);
	}
	
	@GetMapping("/{productId}")
	public ResponseEntity<ProductDto> getProduct(@PathVariable("productId") String productId){
		ProductDto productDto1=productService.get(productId);
		return new ResponseEntity<>(productDto1, HttpStatus.OK);
	}
	
	@DeleteMapping("/{productId}")
	public ResponseEntity<ApiResponseMessage> deleteProduct(@PathVariable("productId") String productId){
		productService.delete(productId);
		ApiResponseMessage apiResponseMessage=ApiResponseMessage.builder().message("Product deleted successfully").status(HttpStatus.OK).success(true).build();
		return new ResponseEntity<>(apiResponseMessage,HttpStatus.OK);
	}
	
	@GetMapping
	public ResponseEntity<PageableResponse<ProductDto>> getAllProducts(
			@RequestParam(value="pageNumber", defaultValue="0", required=false) int pageNumber,
			@RequestParam(value="pageSize", defaultValue="2", required=false) int pageSize,
			@RequestParam(value="sortBy", defaultValue="title", required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue="asc", required=false) String sortDir){
		PageableResponse<ProductDto> pageableResponse=productService.getAll(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}
	
	@GetMapping("/live")
	public ResponseEntity<PageableResponse<ProductDto>> getAllLive(
			@RequestParam(value="pageNumber", defaultValue="0", required=false) int pageNumber,
			@RequestParam(value="pageSize", defaultValue="2", required=false) int pageSize,
			@RequestParam(value="sortBy", defaultValue="title", required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue="asc", required=false) String sortDir){
		PageableResponse<ProductDto> pageableResponse=productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}
	
	@GetMapping("/search/{query}")
	public ResponseEntity<PageableResponse<ProductDto>> searchProduct(
			@PathVariable("query") String query,
			@RequestParam(value="pageNumber", defaultValue="0", required=false) int pageNumber,
			@RequestParam(value="pageSize", defaultValue="2", required=false) int pageSize,
			@RequestParam(value="sortBy", defaultValue="title", required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue="asc", required=false) String sortDir){
		PageableResponse<ProductDto> pageableResponse=productService.searchByTitle(query, pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(pageableResponse, HttpStatus.OK);
	}
	
	//upload product image
		@PostMapping("/image/{productId}")
		public ResponseEntity<ImageResponse> uploadProductImage(@RequestParam("imageName") MultipartFile image, @PathVariable("productId") String productId) throws IOException{
			String imageName=fileService.uploadFile(image, imageUploadPath);
			ProductDto product=productService.get(productId);
			product.setImageName(imageName);
			ProductDto productDto=productService.update(product, productId);
			ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).message("images successfully uploaded").success(true).status(HttpStatus.CREATED).build();
			return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
		}
		
		@GetMapping("/image/{productId}")
		public void serveProductImage(@PathVariable String productId, HttpServletResponse response) throws IOException {
			ProductDto product=productService.get(productId);
			InputStream resource=fileService.getResource(imageUploadPath, product.getImageName());
			response.setContentType(MediaType.IMAGE_JPEG_VALUE);
			StreamUtils.copy(resource,(response.getOutputStream()));
		}
}
