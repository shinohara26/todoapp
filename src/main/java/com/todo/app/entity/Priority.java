package com.todo.app.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 優先度エンティティ
 * 
 * priority_m_tableに対応するクラス
 * タスクの優先度(高・中・低など)を管理する
 */
@Data
@Setter
@Getter
public class Priority {
	/** 優先度ID（主キー）*/
	private Integer priority_id;
	/** 優先度名 */
	private String priority_name;

}
