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
@Table(name="categories")
public class Category {

	@Id
	@Column(name="category_id")
	private String categoryId;
	@Column(name="category_title",length=30, nullable=false)
	private String title;
	@Column(name="category_description", length=100)
	private String description;
	private String coverImage;
}
