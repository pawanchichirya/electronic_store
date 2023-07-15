package com.pc.electronic.store.dtos;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.pc.electronic.store.validate.ImageNameValid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
	
	private String userId;
	@Size(min=3, max=30, message="Invalid Name !!")
	private String name;
	@NotBlank(message="Email is required !!")
	@Pattern(regexp="^[a-z0-9][-a-z0-9._]+@([-a-z0-9]+\\.)+[a-z]{2,5}$", message="Invalid User Email !!")
	private String email;
	@NotBlank(message="Password is required !!")
	@Size(min=10, max=50, message="Password should be between 10 to 50 characters !!")
	private String password;
	@Size(min=4, max=6, message="Invalid Gender !!")
	private String gender;
	@NotBlank(message="Write something about yourself !!")
	private String about;
	
	@ImageNameValid()
	private String imageName;
}
