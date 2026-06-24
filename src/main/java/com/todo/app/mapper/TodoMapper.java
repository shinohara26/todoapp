package com.todo.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.todo.app.entity.Todo;

/**
 * Todoテーブル操作用Mapper
 * CRUDおよび親子タスク取得処理を担当する
 */
@Mapper
public interface TodoMapper {

	/**
	 * 全件取得
	 * 
	 * JOINにより優先度名・カテゴリ名も取得する
	 * 
	 * @return Todo一覧
	 */
	public List<Todo> selectAll();

	/**
	 * 完了/未完了タスク取得(共通化)
	 * 
	 * doneFlgによって取得対象を切り替える
	 * @param doneFlg true=完了済み, false=未完了
	 * @return 対象タスク一覧
	 */
	public List<Todo> selectByDoneFlg(Boolean doneFlg);

	/**
	 * 親子タスクに紐づく子タスク一覧取得
	 * 
	 * @param parent_id 親タスクID
	 * @return 子タスク一覧
	 */
	public List<Todo> selectChildList(Long parent_id);

	/**
	 * ID指定で1件取得
	 * 
	 * @param id タスクID
	 * @return Todo
	 */
	Todo selectById(Long id);

	/**
	 * タスク登録
	 * 
	 * useGeneratedKeysにより自動採番IDがセットされる
	 * 
	 * @param todo 登録対象
	 */
	public void add(Todo todo);
	
	/**
	 * タスク更新
	 * 
	 * @param todo 更新対象
	 */
	public void update(Todo todo);

	/**
	 * 完了済みタスク削除
	 * 
	 * done_flg=trueのデータ削除
	 */
	public void delete();

}