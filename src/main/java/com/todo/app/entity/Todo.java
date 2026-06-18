package com.todo.app.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Todo {


	private Long id;
	private String title;
	private Boolean done_flg;
	private String work_plan_day;
	
	private Long parent_id;
	private String memo;

	private Integer priority;
	private String priority_name;
	
	private Integer category;
	private String category_name;
}