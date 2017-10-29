package com.wst.model.article;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wst.model.user.UserTO;

@Service
public class ArticleService {
	
	@Autowired
	private ArticleRepository articleRepository;
	
	
	public Integer countByAuthor(UserTO author) {
		return articleRepository.countByAuthor(author);
	}
	
	public List<ArticleTO> getAllArticles() {
		List<ArticleTO> articles = new ArrayList<ArticleTO>();
		articleRepository.findAll().forEach(articles::add);
		return articles;
	}
	
	public ArticleTO getArticle(Integer id) {
		return articleRepository.findOne(id);
	}
	
	public void addArticle(ArticleTO article) {
		articleRepository.save(article);
	}
	
	public void updateArticle(ArticleTO article) {
		articleRepository.save(article);
	}
	
	public void deleteArticle(Integer id) {
		articleRepository.delete(id);
	}

}