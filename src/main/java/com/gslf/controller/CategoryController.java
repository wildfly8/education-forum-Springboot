package com.gslf.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.gslf.model.article.ArticleService;
import com.gslf.model.article.ArticleTO;
import com.gslf.model.category.CategoryService;
import com.gslf.model.category.CategoryTO;
import com.gslf.model.topic.TopicService;
import com.gslf.model.topic.TopicTO;
import com.gslf.model.user.UserService;
import com.gslf.model.user.UserTO;
import com.gslf.websecurity.SecurityService;

@Controller
public class CategoryController {
	
	@Autowired
    private SecurityService securityService;
	
    @Autowired
    private UserService userService;
    
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private ArticleService articleService;
	
	
	@RequestMapping(value = "/forum/category-content", method = RequestMethod.GET)
	public String categoryContent(Model model, @RequestParam Integer id) {
		CategoryTO category = categoryService.getCategory(id);
		model.addAttribute("category", category);
		model.addAttribute("role", userService.findByUsername(securityService.findLoggedInUsername()).getRoles().iterator().next());
		return "thymeleaf/category/category-content";
	}
	
	@RequestMapping(value = "/stats", method = RequestMethod.GET)
	public String categoryList(Model model) {
		List<CategoryTO> allCategories = categoryService.getAllCategories();
		List<TopicTO> allTopics = topicService.getAllTopics();
		List<ArticleTO> allArticles = articleService.getAllArticles();
		List<UserTO> allUsers = userService.getAllUsers();
		model.addAttribute("allCategories", allCategories);
		model.addAttribute("allTopics", allTopics);
		model.addAttribute("allArticles", allArticles);
		model.addAttribute("allUsers", allUsers);		
		return "thymeleaf/stats";
	}
	
	@RequestMapping(value = "/forum/admin/category-add", method = RequestMethod.GET)
	public String addInput() {
		return "thymeleaf/category/category-add";
	}
	
	@RequestMapping(value = "/forum/admin/category-add", method = RequestMethod.POST)
	public ModelAndView add(HttpServletRequest request, RedirectAttributes redir) {
		String categoryName = request.getParameter("categoryname");
		String categoryDesc = request.getParameter("categorydescription");
		CategoryTO category = new CategoryTO(null, categoryName, categoryDesc);
		categoryService.addCategory(category);
	    redir.addFlashAttribute("category", category);
        return new ModelAndView(new RedirectView("category-added", true));
	}
	
	@RequestMapping(value = "/forum/admin/category-added", method = RequestMethod.GET)
	public String added() {
		return "thymeleaf/category/category-added";
	}
	
	@RequestMapping(value = "/forum/admin/category-update", method = RequestMethod.GET)
	public String updateInput(Model model, @RequestParam("id") Integer id) {
		CategoryTO category = categoryService.getCategory(id);
		model.addAttribute("category", category);
	    return "thymeleaf/category/category-update";
	}
	
	@RequestMapping(value = "/forum/admin/category-update", method = RequestMethod.POST)
	public ModelAndView update(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("categoryid"));
		String categoryName = request.getParameter("categoryname");
		String categoryDesc = request.getParameter("categorydescription");
		CategoryTO category = new CategoryTO(id, categoryName, categoryDesc);
		categoryService.updateCategory(category.getId(), category);
        return new ModelAndView(new RedirectView("/forum", true));
	}
	
	@RequestMapping(value = "/forum/admin/category-delete", method = RequestMethod.POST)
	public ModelAndView delete(@RequestParam("id") Integer id) {
		categoryService.deleteCategory(id);
        return new ModelAndView(new RedirectView("/forum", true));
	}
	
}
