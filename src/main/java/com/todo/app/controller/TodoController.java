package com.todo.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.todo.app.entity.Category;
import com.todo.app.entity.Priority;
import com.todo.app.entity.Todo;
import com.todo.app.mapper.CategoryMapper;
import com.todo.app.mapper.PriorityMapper;
import com.todo.app.mapper.TodoMapper;

/**
 * Todoアプリのコントローラー
 * 画面遷移やCRUD処理を担当する
 * 
 * ・一覧表示
 * 追加(ajax)
 * 更新
 * 削除
 * 詳細表示
 */
@Controller
public class TodoController {
	/**	ログ出力用 */
	private static final Logger logger = LoggerFactory.getLogger(TodoController.class);
	
	/** Todoデータアクセス */
	@Autowired
	TodoMapper todoMapper;
	
	/** 優先度マスタ */
	@Autowired
	PriorityMapper priorityMapper;
	
	/** カテゴリマスタ */
	@Autowired
	CategoryMapper categoryMapper;
	
	/**
	 * トップ画面表示
	 * 未完了・完了タスクを取得し一覧表示する
	 * 
	 * @param model Thymeleafへデータを渡すためのモデル
	 * @return index画面
	 */

	@RequestMapping(value = "/")
	public String index(Model model) {
		logger.info("アクセス: /");
		logger.info("ログ出力aaa");

		// 未完了タスク取得
		List<Todo> list = todoMapper.selectByDoneFlg(false);
		// 完了タスク取得
		List<Todo> doneList = todoMapper.selectByDoneFlg(true);
		
		// マスタデータ取得
		List<Priority> priorityList = priorityMapper.selectAll();
		List<Category> categoryList = categoryMapper.selectAll();

		// デバッグ用(通常はfalseで未実行)
		if (false) {
			logger.debug("list:" + list.size() + "件");
			logger.debug("doneList:" + doneList.size() + "件");
		}

		// 画面にデータを渡す
		model.addAttribute("todos", list);
		model.addAttribute("doneTodos", doneList);

		model.addAttribute("priorityList", priorityList);
		model.addAttribute("categoryList", categoryList);

		return "index";
	}
	
	/**
	 * タスク追加処理(Ajax)
	 * 
	 * バリデーションを行い、結果をJSON形式で返却する
	 * 
	 * @param todo 入力されたタスク情報
	 * @param result バリデーション結果
	 * @return 処理結果(JSON)
	 */
	@RequestMapping(value = "/add")
	@ResponseBody
	public Map<String, Object> add(@Valid Todo todo, BindingResult result) {
		logger.info("アクセス: /add");

		Map<String, Object> response = new HashMap<>();

		// バリデーションエラーがある場合
		if (result.hasErrors()) {
			response.put("success", false);
			
			//フィールドごとのエラーメッセージをまとめる
			Map<String, String> errorMap = new HashMap<>();
			for (FieldError error : result.getFieldErrors()) {
				errorMap.put(error.getField(), error.getDefaultMessage());
			}
			response.put("errors", errorMap);
			return response;
		}

		// DBへ登録
		todoMapper.add(todo);

		logger.debug("追加後 id = " + todo.getId());
		logger.debug("parent_id = " + todo.getParent_id());

		// 正常終了
		response.put("success", true);
		response.put("id", todo.getId());
		return response;

	}

	/** 
	 * タスク更新処理
	 * 
	 * 更新後、親タスクに応じて遷移先を変更する
	 * 
	 * @param todo 更新対象のタスク
	 * @param result バリデーション結果
	 * @param model 画面へ渡すデータ
	 * @return 遷移先画面
	 */
	@RequestMapping(value = "/update")
	public String update(@Validated Todo todo, BindingResult result, Model model) {
		
		// ログ(デバッグ用)
		logger.debug("アクセス: /update");
		logger.debug("id=" + todo.getId());
		logger.debug("title=" + todo.getTitle());
		logger.debug("done_flg=" + todo.getDone_flg());
		logger.debug("priority=" + todo.getPriority());
		logger.debug("category=" + todo.getCategory());
		logger.debug("work_plan_day=" + todo.getWork_plan_day());
		logger.debug("memo=" + todo.getMemo());

		// バリデーションエラー時
		if (result.hasErrors()) {
			List<Priority> priorityList = priorityMapper.selectAll();
			List<Category> categoryList = categoryMapper.selectAll();

			model.addAttribute("todo", todo);
			model.addAttribute("priorityList", priorityList);
			model.addAttribute("categoryList", categoryList);

			// 子タスクの場合
			if (todo.getParent_id() != null) {
				return "child_detail";
			}

			// 親タスクの場合は子タスク一覧も表示
			List<Todo> childTodos = todoMapper.selectChildList(todo.getId());
			model.addAttribute("childTodos", childTodos);
			return "detail";
		}

		// 正常時は更新処理
		todoMapper.update(todo);

		// 親子で遷移先を変更
		if (todo.getParent_id() != null) {
			return "redirect:/child/detail?id=" + todo.getId();
		} else {
			return "redirect:/detail?id=" + todo.getId();
		}
	}

	/**
	 * 完了済みタスク削除処理(Ajax)
	 * done_flg=trueのデータを削除する
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public void delete() {
		logger.info("アクセス: /delete");

		// 完了済みタスク削除
		todoMapper.delete();
	}

	/**
	 * 親タスクの詳細画面表示
	 * 
	 * @param id タスクID
	 * @param model 画面へ渡すデータ
	 * @return detail画面
	 */
	@RequestMapping(value = "/detail")
	public String detail(Long id, Model model) {
		logger.info("アクセス: /detail");

		// タスク取得
		Todo todo = todoMapper.selectById(id);
		
		// マスクデータ取得
		List<Priority> priorityList = priorityMapper.selectAll();
		List<Category> categoryList = categoryMapper.selectAll();
		
		// 子タスク取得
		List<Todo> childTodos = todoMapper.selectChildList(id);

		model.addAttribute("todo", todo);
		model.addAttribute("priorityList", priorityList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("childTodos", childTodos);
		return "detail";
	}
	
	/**
	 * 子タスクの詳細画面表示
	 * 
	 * @param id タスクID
	 * @param model 画面へ渡すデータ
	 * @return child_detail画面
	 */
	@RequestMapping(value = "/child/detail")
	public String childDetail(Long id, Model model) {
		logger.info("アクセス: /child/detail");

		// 子タスク取得
		Todo todo = todoMapper.selectById(id);

		// マスタ取得
		List<Priority> priorityList = priorityMapper.selectAll();
		List<Category> categoryList = categoryMapper.selectAll();

		model.addAttribute("todo", todo);
		model.addAttribute("priorityList", priorityList);
		model.addAttribute("categoryList", categoryList);
		return "child_detail";
	}
}