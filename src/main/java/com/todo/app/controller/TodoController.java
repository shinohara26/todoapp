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

//				List<Todo> list = todoMapper.selectAll();

				List<Todo> list = todoMapper.selectIncomplete();
				List<Todo> doneList = todoMapper.selectComplete();

//		List<Todo> parentTodos = todoMapper.selectParentInComplete();
//		List<Todo> childTodos = todoMapper.selectChildInComplete();
//
//		List<Todo> parentDoneTodos = todoMapper.selectParentComplete();
//		List<Todo> childDoneTodos = todoMapper.selectChildComplete();

		List<Priority> priorityList = priorityMapper.selectAll();
		List<Category> categoryList = categoryMapper.selectAll();

//		Map<Long, List<Todo>> childMap = new HashMap<>();
//		for (Todo child : childTodos) {
//			childMap.computeIfAbsent(child.getParent_id(), k -> new ArrayList<>()).add(child);
//		}
//		Map<Long, List<Todo>> childDoneMap = new HashMap<>();
//		for (Todo child : childDoneTodos) {
//			childDoneMap.computeIfAbsent(child.getParent_id(), k -> new ArrayList<>()).add(child);
//		}

				if (false) {
					logger.debug("list:" + list.size() + "件");
					logger.debug("doneList:" + doneList.size() + "件");
				}

				model.addAttribute("todos", list);
				model.addAttribute("doneTodos", doneList);

//		model.addAttribute("parentTodos", parentTodos);
//		model.addAttribute("childMap", childMap);
//
//		model.addAttribute("parentDoneTodos", parentDoneTodos);
//		model.addAttribute("childDoneMap", childDoneMap);

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
			Map<String, String> errorMap = new HashMap<>();
	        for (FieldError error : result.getFieldErrors()) {
	            errorMap.put(error.getField(), error.getDefaultMessage());
	        }
			response.put("errors", errorMap);
			return response;
		}

		todoMapper.add(todo);

		logger.debug("追加後 id = " + todo.getId());
		logger.debug("parent_id = " + todo.getParent_id());

		response.put("success", true);
		response.put("id", todo.getId());
		return response;

	}

	@RequestMapping(value = "/update")
	//	@ResponseBody
	public String update(@Validated Todo todo, BindingResult result, Model model) {
		logger.debug("アクセス: /update");
		logger.debug("id=" + todo.getId());
		logger.debug("title=" + todo.getTitle());
		logger.debug("done_flg=" + todo.getDone_flg());
		logger.debug("priority=" + todo.getPriority());
		logger.debug("category=" + todo.getCategory());
		logger.debug("work_plan_day=" + todo.getWork_plan_day());
		logger.debug("memo=" + todo.getMemo());
		
		if(result.hasErrors()) {
			List<Priority> priorityList = priorityMapper.selectAll();
			List<Category> categoryList = categoryMapper.selectAll();
			
			model.addAttribute("todo", todo);
			model.addAttribute("priorityList", priorityList);
			model.addAttribute("categoryList", categoryList);
			
			if(todo.getParent_id() != null) {
				return "child_detail";
			}
			
			List<Todo> childTodos = todoMapper.selectChildList(todo.getId());
			model.addAttribute("childTodos", childTodos);
			return "detail";
		}

		todoMapper.update(todo);

		if (todo.getParent_id() != null) {
			return "redirect:/child/detail?id=" + todo.getId();
		} else {
			return "redirect:/detail?id=" + todo.getId();
		}
	}

	@RequestMapping(value = "/delete")
	@ResponseBody
	public void delete() {
		logger.info("アクセス: /delete");

		todoMapper.delete();
	}

	@RequestMapping(value = "/detail")
	public String detail(Long id, Model model) {
		logger.info("アクセス: /detail");

		Todo todo = todoMapper.selectById(id);
		List<Priority> priorityList = priorityMapper.selectAll();
		List<Category> categoryList = categoryMapper.selectAll();
		List<Todo> childTodos = todoMapper.selectChildList(id);

		model.addAttribute("todo", todo);
		model.addAttribute("priorityList", priorityList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("childTodos", childTodos);
		return "detail";
	}

	@RequestMapping(value = "/child/detail")
	public String childDetail(Long id, Model model) {
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