package com.pc.electronic.store.controllers;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.ServletResponse;
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
import com.pc.electronic.store.dtos.ImageResponse;
import com.pc.electronic.store.dtos.PageableResponse;
import com.pc.electronic.store.dtos.UserDto;
import com.pc.electronic.store.services.FileService;
import com.pc.electronic.store.services.UserService;


@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;
	
	@Value("${user.profile.image.path}")
	private String imageUploadPath;
	//create
	@PostMapping
	public ResponseEntity<UserDto> createUser( @Valid @RequestBody UserDto userDto){
		UserDto userDto1=userService.createUser(userDto);
		return new ResponseEntity<>(userDto1,HttpStatus.CREATED);
	}
	
	//update
	@PutMapping("/{userId}")
	public ResponseEntity<UserDto> updateUser(@PathVariable("userId") String userId,@Valid @RequestBody UserDto userDto){
		UserDto userDto1=userService.updateUser(userDto, userId);
		return new ResponseEntity<>(userDto1, HttpStatus.OK);
	}
	
	//delete
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponseMessage> deleteUser(@PathVariable("userId") String userId){
		userService.deleteUser(userId);
		ApiResponseMessage message=ApiResponseMessage.builder().message("user is deleted successfully").success(true).status(HttpStatus.OK).build();
		return new ResponseEntity<>(message, HttpStatus.OK);
	}
	
	//get all
	@GetMapping
	public ResponseEntity<PageableResponse<UserDto>> getAllUsers(@RequestParam(value="pageNumber", defaultValue="0",required=false) int pageNumber, 
			@RequestParam(value="pageSize", defaultValue="2",required=false) int pageSize,
			@RequestParam(value="sortBy", defaultValue="name",required=false) String sortBy,
			@RequestParam(value="sortDir", defaultValue="asc", required=false) String sortDir){
		PageableResponse<UserDto> userDtoList=userService.getAllUsers(pageNumber, pageSize, sortBy, sortDir);
		return new ResponseEntity<>(userDtoList, HttpStatus.OK);
	}
	//get single
	@GetMapping("/{userId}")
	public ResponseEntity<UserDto> getUser(@PathVariable("userId") String userId){
		UserDto user=userService.getUserById(userId);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	//get by email
	@GetMapping("/email/{email}")
	public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
		UserDto user=userService.getUserByEmail(email);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	//search user
	@GetMapping("/search/{keywords}")
	public ResponseEntity<List<UserDto>> searchUser(@PathVariable String keywords){
		return new ResponseEntity<>(userService.searchUser(keywords),HttpStatus.OK);
	}
	
	//upload user image
	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(@RequestParam("userImage") MultipartFile image, @PathVariable("userId") String userId) throws IOException{
		String imageName=fileService.uploadFile(image, imageUploadPath);
		UserDto user=userService.getUserById(userId);
		user.setImageName(imageName);
		UserDto userDto=userService.updateUser(user, userId);
		ImageResponse imageResponse=ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED).build();
		return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);
	}
	
	@GetMapping("/image/{userId}")
	public void serveUserImage(@PathVariable String userId, HttpServletResponse response) throws IOException {
		UserDto user=userService.getUserById(userId);
		InputStream resource=fileService.getResource(imageUploadPath, user.getImageName());
		response.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(resource,(response.getOutputStream()));
	}
}
