package com.todo.app.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Todo {


	private long id;
	private String title;
	private boolean done_flg;
	private String work_plan_day;
	private long priority;
	private long category;
	private long parent_id;
	private String memo;
	
	private String priority_name;
	private String category_name;
}