package com.pc.electronic.store.services.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.pc.electronic.store.dtos.PageableResponse;
import com.pc.electronic.store.dtos.UserDto;
import com.pc.electronic.store.entities.User;
import com.pc.electronic.store.exceptions.ResourceNotFoundException;
import com.pc.electronic.store.helper.Helper;
import com.pc.electronic.store.repositories.UserRepository;
import com.pc.electronic.store.services.UserService;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private ModelMapper mapper;

	@Autowired
	private UserRepository userRepository;
	
	@Value("${user.profile.image.path}")
	private String imagePath;
	
	@Override
	public UserDto createUser(UserDto userDto) {
		// TODO Auto-generated method stub
		String userId=UUID.randomUUID().toString();
		userDto.setUserId(userId);
		User user=dtoToEntity(userDto);
		User savedUser=userRepository.save(user);
		UserDto savedUserDto=entityToDto(savedUser);
		return savedUserDto;
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {
		// TODO Auto-generated method stub
		User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("no user found with such id"));
		user.setName(userDto.getName());
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		user.setImageName(userDto.getImageName());
		user.setPassword(userDto.getPassword());
		
		User updatedUser=userRepository.save(user);
		UserDto updatedUserDto=entityToDto(updatedUser);
		return updatedUserDto;
	}

	@Override
	public void deleteUser(String usrId) {
		// TODO Auto-generated method stub
		User user=userRepository.findById(usrId).orElseThrow(()-> new ResourceNotFoundException("no user found with such id"));
		
		//delete image from folder when user is deleted from backend
		String fullPath=imagePath+ user.getImageName();
		try {
			Path path=Paths.get(fullPath);
			Files.delete(path);
		} catch(NoSuchFileException ex) {
			ex.printStackTrace();
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		userRepository.delete(user);
	}

	@Override
	public PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// TODO Auto-generated method stub
		Sort sort=(sortDir.equalsIgnoreCase("asc"))?(Sort.by(sortBy).ascending()):(Sort.by(sortBy).descending());
		Pageable pageable=PageRequest.of(pageNumber, pageSize,sort);
		Page<User> page=userRepository.findAll(pageable);
		PageableResponse<UserDto> response=Helper.getPageableResponse(page, UserDto.class);
		return response;
	}

	@Override
	public UserDto getUserById(String userId) {
		// TODO Auto-generated method stub
		User user=userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("no user found with such id"));
		UserDto userDto=entityToDto(user);
		return userDto;
	}

	@Override
	public UserDto getUserByEmail(String email) {
		// TODO Auto-generated method stub
		User user=userRepository.findByEmail(email).orElseThrow(()-> new ResourceNotFoundException("No user found with such email id"));
		UserDto userDto=entityToDto(user);
		return userDto;
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		// TODO Auto-generated method stub
		List<User> users=userRepository.findByNameContaining(keyword).orElseThrow(()-> new ResourceNotFoundException("No user found"));
		List<UserDto> userDtoList=users.stream().map(user->entityToDto(user)).collect(Collectors.toList());
		return userDtoList;
	}
	
	private UserDto entityToDto(User user) {
//		UserDto userDto=UserDto.builder()
//				.userId(user.getUserId())
//				.name(user.getName())
//				.email(user.getEmail())
//				.password(user.getPassword())
//				.about(user.getAbout())
//				.gender(user.getGender())
//				.imageName(user.getImageName())
//				.build();
		return mapper.map(user,UserDto.class);
	}
	
	private User dtoToEntity(UserDto userDto) {
//		User user=User.builder().userId(userDto.getUserId())
//				.name(userDto.getName())
//				.email(userDto.getEmail())
//				.password(userDto.getPassword())
//				.about(userDto.getAbout())
//				.gender(userDto.getGender())
//				.imageName(userDto.getImageName())
//				.build();
		return mapper.map(userDto, User.class);
	}

}
