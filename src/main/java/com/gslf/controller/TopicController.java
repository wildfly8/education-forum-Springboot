package com.gslf.controller;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.gslf.model.article.ArticleService;
import com.gslf.model.article.ArticleTO;
import com.gslf.model.category.CategoryService;
import com.gslf.model.category.CategoryTO;
import com.gslf.model.topic.TopicService;
import com.gslf.model.topic.TopicTO;
import com.gslf.model.user.UserService;
import com.gslf.websecurity.SecurityService;

@Controller
public class TopicController {
	
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
	
	
	@RequestMapping(value = "/forum/topic-add", method = RequestMethod.GET)
	public String addTopic(Model model, @RequestParam Integer categoryId) {
		CategoryTO category = categoryService.getCategory(categoryId);
		model.addAttribute("category", category);
		return "thymeleaf/topic/topic-add";
	}
	
	@RequestMapping(value = "/forum/topic-add", method = RequestMethod.POST)
	public ModelAndView postTopic(HttpServletRequest request, @RequestParam Integer categoryId) {
		String title = request.getParameter("title");
		String content = request.getParameter("summernoteContent");
		CategoryTO category = categoryService.getCategory(categoryId);
		TopicTO topic = new TopicTO(null, title, category);
		Date now = Calendar.getInstance().getTime();
		ArticleTO article = new ArticleTO(null, title, content, 0, now, now, topic, userService.findByUsername(securityService.findLoggedInUsername()));
		topicService.save(topic);
		articleService.addArticle(article);
        return new ModelAndView(new RedirectView("article-list?topicId=" + topic.getId(), true));
	}
	
	@RequestMapping(value = "/forum/topic-delete", method = RequestMethod.GET)
	public ModelAndView deleteTopic(@RequestParam Integer topicId) {
		TopicTO topic = topicService.getTopic(topicId);
		topicService.deleteTopic(topicId);
        return new ModelAndView(new RedirectView("category-content?id=" + topic.getCategory().getId(), true));
	}
	
}
