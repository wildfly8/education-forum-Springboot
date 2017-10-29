package com.wst.controller;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.wst.model.article.ArticleService;
import com.wst.model.article.ArticleTO;
import com.wst.model.topic.TopicService;
import com.wst.model.topic.TopicTO;
import com.wst.model.user.UserService;
import com.wst.model.user.UserTO;
import com.wst.websecurity.SecurityService;

@Controller
public class ArticleController {
	
	private static final Logger logger = LoggerFactory.getLogger(ArticleController.class);

	@Autowired
    private SecurityService securityService;
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private TopicService topicService;
	
	@Autowired
	private ArticleService articleService;
	
	
	@RequestMapping(value = "/forum/article-list", method = RequestMethod.GET)
	public String getAllArticles(@RequestParam Integer topicId, Model model) {
		TopicTO topic = topicService.getTopic(topicId);
		for(ArticleTO article : topic.getArticles()) {
			article.getAuthor().setNumOfPosts(articleService.countByAuthor(article.getAuthor()));	
			if(article.getAuthor().getIconBlob() != null) {
		    	try {
		            byte[] encodeBase64 = Base64.getEncoder().encode(article.getAuthor().getIconBlob().getBytes(1L, (int) article.getAuthor().getIconBlob().length()));
		            article.getAuthor().setBase64imageString(new String(encodeBase64, "UTF-8"));	
		    	} catch(UnsupportedEncodingException e) {
					logger.error(e.getMessage());
		    	} catch (SQLException e) {
					logger.error(e.getMessage());
				}	
			}
		}
		UserTO user = userService.findByUsername(securityService.findLoggedInUsername());
		if(user.getIconBlob() != null) {
	    	try {
	            byte[] encodeBase64 = Base64.getEncoder().encode(user.getIconBlob().getBytes(1L, (int) user.getIconBlob().length()));
	            user.setBase64imageString(new String(encodeBase64, "UTF-8"));	
	    	} catch(UnsupportedEncodingException e) {
				logger.error(e.getMessage());
	    	} catch (SQLException e) {
				logger.error(e.getMessage());
			}	
		}
		model.addAttribute("topic", topic);
		model.addAttribute("user", user);
		return "thymeleaf/article/article-list";
	}
	
	@RequestMapping(value = "/forum/article-list", method = RequestMethod.POST)
	public ModelAndView replyTopic(HttpServletRequest request, @RequestParam Integer topicId) {
		String articleContent = request.getParameter("summernoteContent");
		TopicTO topic = topicService.getTopic(topicId);
		int numOfArticles = topic.getArticles().size();
		Date now = Calendar.getInstance().getTime();
		ArticleTO article = new ArticleTO(null, "RE: " + topic.getName(), articleContent, numOfArticles, now, now, topic, userService.findByUsername(securityService.findLoggedInUsername()));
		articleService.addArticle(article);
        return new ModelAndView(new RedirectView("article-list?topicId=" + topicId, true));
	}
	
	@RequestMapping(value = "/forum/article-update", method = RequestMethod.GET)
	public String getUpdateArticle(Model model, @RequestParam Integer id) {
		ArticleTO article = articleService.getArticle(id);
		model.addAttribute("article", article);
		return "thymeleaf/article/article-update";
	}
	
	@RequestMapping(value = "/forum/article-update", method = RequestMethod.POST)
	public ModelAndView updateArticle(HttpServletRequest request, @RequestParam Integer id) {
		ArticleTO article = articleService.getArticle(id);
		String updatedTitle = request.getParameter("title");
		String updatedContent = request.getParameter("summernoteContent");
		Date now = Calendar.getInstance().getTime();
		article.setLastUpdate(now);
		article.setTitle(updatedTitle);
		article.setContent(updatedContent);
		articleService.updateArticle(article);
        return new ModelAndView(new RedirectView("article-list?topicId=" + articleService.getArticle(id).getTopic().getId(), true));
	}
	
	@RequestMapping(value = "/forum/article-reply", method = RequestMethod.GET)
	public String getArticleReply(Model model, @RequestParam Integer id) {
		ArticleTO article = articleService.getArticle(id);
		article.getAuthor().setNumOfPosts(articleService.countByAuthor(article.getAuthor()));
		if(article.getAuthor().getIconBlob() != null) {
	    	try {
	            byte[] encodeBase64 = Base64.getEncoder().encode(article.getAuthor().getIconBlob().getBytes(1L, (int) article.getAuthor().getIconBlob().length()));
	            article.getAuthor().setBase64imageString(new String(encodeBase64, "UTF-8"));	
	    	} catch(UnsupportedEncodingException e) {
				logger.error(e.getMessage());
	    	} catch (SQLException e) {
				logger.error(e.getMessage());
			}
		}
		UserTO user = userService.findByUsername(securityService.findLoggedInUsername());
		if(user.getIconBlob() != null) {
	    	try {
	            byte[] encodeBase64 = Base64.getEncoder().encode(user.getIconBlob().getBytes(1L, (int) user.getIconBlob().length()));
	            user.setBase64imageString(new String(encodeBase64, "UTF-8"));	
	    	} catch(UnsupportedEncodingException e) {
				logger.error(e.getMessage());
	    	} catch (SQLException e) {
				logger.error(e.getMessage());
			}	
		}
		model.addAttribute("article", article);
		model.addAttribute("user", user);
		return "thymeleaf/article/article-reply";
	}
	
	@RequestMapping(value = "/forum/article-reply", method = RequestMethod.POST)
	public ModelAndView replyArticle(HttpServletRequest request, @RequestParam Integer id) {
		ArticleTO article = articleService.getArticle(id);
		String newContent = request.getParameter("summernoteContent");
		Date now = Calendar.getInstance().getTime();
		TopicTO topic = article.getTopic();
		int numOfArticles = topic.getArticles().size();
		ArticleTO newArticle = new ArticleTO(null, "RE: Level-" + (article.getGrade() + 1), newContent, numOfArticles, now, now, topic, userService.findByUsername(securityService.findLoggedInUsername()));
		articleService.addArticle(newArticle);
        return new ModelAndView(new RedirectView("article-list?topicId=" + articleService.getArticle(id).getTopic().getId(), true));
	}
	
	@RequestMapping(value = "/forum/article-delete", method = RequestMethod.GET)
	public ModelAndView deleteArticle(@RequestParam Integer id) {
		ArticleTO articleToBeDeleted = articleService.getArticle(id);
		int topicId = articleToBeDeleted.getTopic().getId();
		articleService.deleteArticle(id);
        return new ModelAndView(new RedirectView("article-list?topicId=" + topicId, true));
	}
	
}
