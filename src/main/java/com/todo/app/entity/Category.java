package com.todo.app.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * カテゴリエンティティ
 * 
 * category_m_tableに対応するクラス
 * カテゴリのマスタ情報(ID・名称)を保持する
 * 主に画面のプルダウン表示に使用される
 */
@Data
@Setter
@Getter
public class Category {
	
	/** カテゴリID(主キー) */
	private Integer category_id;
	/** カテゴリ名 */
	private String category_name;
}
