package com.todo.app.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class Category {
	private Integer category_id;
	private String category_name;
}
