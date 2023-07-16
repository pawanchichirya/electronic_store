package com.pc.electronic.store.dtos;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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

public class CategoryDto {
	private String categoryId;
	@NotBlank(message="Title is Required !!")
	@Size(min=3, max=30, message="Tlte should be between 3 to 30 chracters !!")
	private String title;
	@NotBlank(message="Description is required !!")
	private String description;
	@ImageNameValid()
	private String coverImage;
}
