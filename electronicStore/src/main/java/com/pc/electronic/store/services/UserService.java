package com.pc.electronic.store.services;

import java.util.List;

import com.pc.electronic.store.dtos.PageableResponse;
import com.pc.electronic.store.dtos.UserDto;

public interface UserService {
	UserDto createUser(UserDto userDto);
	UserDto updateUser(UserDto userDto, String userId);
	void deleteUser(String usrId);
	PageableResponse<UserDto> getAllUsers(int pageNumber, int pageSize, String sortBy, String sortDir);
	UserDto getUserById(String userId);
	UserDto getUserByEmail(String email);
	List<UserDto> searchUser(String keyword);
}
