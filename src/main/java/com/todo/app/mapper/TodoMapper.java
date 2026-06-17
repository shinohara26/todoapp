package com.todo.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.todo.app.entity.Todo;

@Mapper
public interface TodoMapper {

	public List<Todo> selectAll();

	public List<Todo> selectIncomplete();

	public List<Todo> selectComplete();
	
	Todo selectById(@Param("id") Integer id);

	public void add(Todo todo);

	public void update(Todo todo);

	public void delete();

}