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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.todo.app.entity.Category;
import com.todo.app.entity.Priority;
import com.todo.app.entity.Todo;
import com.todo.app.mapper.CategoryMapper;
import com.todo.app.mapper.PriorityMapper;
import com.todo.app.mapper.TodoMapper;

@Controller
public class TodoController {

	private static final Logger logger = LoggerFactory.getLogger(TodoController.class);

	@Autowired
	TodoMapper todoMapper;
	@Autowired
	PriorityMapper priorityMapper;
	@Autowired
	CategoryMapper categoryMapper;

	@RequestMapping(value = "/")
	public String index(Model model) {
		logger.info("アクセス: /");
		logger.info("ログ出力aaa");

		//		List<Todo> list = todoMapper.selectAll();

		List<Todo> list = todoMapper.selectIncomplete();
		List<Todo> doneList = todoMapper.selectComplete();

		List<Priority> priorityList = priorityMapper.selectAll();
		List<Category> categoryList = categoryMapper.selectAll();

		if (false) {
			logger.debug("list:" + list.size() + "件");
			logger.debug("doneList:" + doneList.size() + "件");
		}

		model.addAttribute("todos", list);
		model.addAttribute("doneTodos", doneList);
		model.addAttribute("priorityList", priorityList);
		model.addAttribute("categoryList", categoryList);

		return "index";
	}

	@RequestMapping(value = "/add")
	@ResponseBody
	public Map<String, Object> add(@Valid Todo todo, BindingResult result) {
		logger.info("アクセス: /add");

		Map<String, Object> response = new HashMap<>();

		if (result.hasErrors()) {
			response.put("success", false);
			response.put("errors", result.getAllErrors());
			return response;
		}

		todoMapper.add(todo);

		logger.info("追加後 id = " + todo.getId());

		response.put("success", true);
		response.put("id", todo.getId());
		return response;

	}

	@RequestMapping(value = "/update")
	//	@ResponseBody
	public String update(Todo todo) {
		logger.info("アクセス: /update");
		logger.info("id=" + todo.getId());
		logger.info("title=" + todo.getTitle());
		logger.info("done_flg=" + todo.getDone_flg());
		logger.info("priority=" + todo.getPriority());
		logger.info("category=" + todo.getCategory());
		logger.info("work_plan_day=" + todo.getWork_plan_day());
		logger.info("memo=" + todo.getMemo());

		todoMapper.update(todo);

		return "redirect:/detail?id=" + todo.getId();
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public void delete() {
		logger.info("アクセス: /delete");

		todoMapper.delete();
	}

	@RequestMapping(value = "/detail")
	public String detail(Integer id, Model model) {
		logger.info("アクセス: /detail");

		Todo todo = todoMapper.selectById(id);
		List<Priority> priorityList = priorityMapper.selectAll();
		List<Category> categoryList = categoryMapper.selectAll();

		model.addAttribute("todo", todo);
		model.addAttribute("priorityList", priorityList);
		model.addAttribute("categoryList", categoryList);
		return "detail";
	}

	@RequestMapping(value = "/child/detail")
	public String childDetail(Integer id, Model model) {
		logger.info("アクセス: /child/detail");

		Todo todo = todoMapper.selectById(id);
		List<Priority> priorityList = priorityMapper.selectAll();
		List<Category> categoryList = categoryMapper.selectAll();

		model.addAttribute("todo", todo);
		model.addAttribute("priorityList", priorityList);
		model.addAttribute("categoryList", categoryList);
		return "child_detail";
	}
}