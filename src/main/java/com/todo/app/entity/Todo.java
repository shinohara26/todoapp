package com.todo.app.entity;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Todoエンティティ
 * 
 * items_tableに対応するクラス
 * タスクの基本情報に加え、親子関係や表示用データも保持する
 * 
 * 親子構造(parent_id)
 * 優先度・カテゴリ(外部キー)
 * 画面表示用の名称(JOIN結果)
 */
@Data
@Getter
@Setter
public class Todo {

	/** タスクID(主キー) */
	private Long id;
	
	/**
	 * タスク名
	 * 空文字不可(バリデーション対象)
	 */
	@NotBlank(message = "タスク名を入力してください")
	private String title;
	
	/**
	 * 完了フラグ
	 * true:完了 / false:完了
	 */
	private Boolean done_flg;
	
	/**
	 * 作業予定日
	 * フロントとの連携のためStringで保持し、
	 * DB側でDATE型へ変換している
	 */
	private String work_plan_day;

	/**
	 * 親タスクID
	 * ・null:親タスク
	 * ・値あり:子タスク
	 */
	private Long parent_id;
	
	/** メモ(補足情報) */
	private String memo;

	/**
	 * 優先度ID(外部キー)
	 * priority_m_tableと紐づく
	 */
	private Integer priority;
	
	/**
	 * 優先度名(JOINで取得)
	 * 画面表示用
	 */
	private String priority_name;

	/**
	 * カテゴリID(外部キー)
	 * category_m_tableと紐づく
	 */
	private Integer category;
	
	/**
	 * カテゴリ名(JOINで取得)
	 * 画面表示用
	 */
	private String category_name;

	/**
	 * 階層レベル(表示制御用)
	 * 0:親タスク
	 * 1:子タスク
	 * SQLのcase文で生成される
	 */
	private Integer level;
}