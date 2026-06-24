package com.todo.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todo.app.entity.Priority;

/**
 * 優先度マスタテーブル用Mapper
 */
@Mapper
public interface PriorityMapper {
	
	/**
	 * 優先度一覧取得
	 * 
	 * 画面のプルダウン表示などに使用
	 * 
	 * @return　優先度一覧
	 */
	public List<Priority> selectAll();

}
