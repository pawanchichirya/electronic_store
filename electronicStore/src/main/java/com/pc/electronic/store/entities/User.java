package com.pc.electronic.store.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@Entity
@Table(name="users")
public class User {
	
	@Id
	private String userId;
	
	@Column(name="user_name")
	private String name;
	@Column(name="user_email", unique=true)
	private String email;
	@Column(name="user_password", length=30)
	private String password;
	
	private String gender;
	@Column(length=1000)
	private String about;
	@Column(name="user_image_name")
	private String imageName;
	
}
