package com.todo.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todo.app.entity.Priority;

@Mapper
public interface PriorityMapper {
	public List<Priority> selectAll();

}
