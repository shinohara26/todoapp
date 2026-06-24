package com.todo.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todo.app.entity.Category;

/**
 * カテゴリマスタテーブル用Mapper
 * MyBatisにより実装クラスは自動生成される
 */
@Mapper
public interface CategoryMapper {
	
	/**
	 * カテゴリ全件取得
	 * 
	 * カテゴリ情報(ID・名称)を取得する
	 * 主に画面のプルダウン表示用
	 * 
	 * @return カテゴリ一覧
	 */
	public List<Category> selectAll();
}
