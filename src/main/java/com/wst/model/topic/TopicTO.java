package com.wst.model.topic;

import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import com.wst.model.article.ArticleTO;
import com.wst.model.category.CategoryTO;

@Entity
@Table(name="t_topic")
public class TopicTO {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@NotNull
	private String name;
	
	private int viewedNumber;

	@ManyToOne
	@JoinColumn(name = "category_id")
	private CategoryTO category;

	@OneToMany(mappedBy = "topic", cascade = {CascadeType.REMOVE})
	private List<ArticleTO> articles;
	

	public TopicTO() {}
	
	public TopicTO(Integer id, String name, CategoryTO category) {
		super();
		this.id = id;
		this.name = name;
		this.category = category;
	}
	
	@Transient
	public ArticleTO getLatestArticle() {
		if(articles == null || articles.isEmpty()) {
			return null;
		} else {
			Collections.sort(articles, Collections.reverseOrder());
			return articles.get(0);
		}
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public CategoryTO getCategory() {
		return category;
	}

	public void setCategory(CategoryTO category) {
		this.category = category;
	}
	
	public List<ArticleTO> getArticles() {
		return articles;
	}

	public int getViewedNumber() {
		return viewedNumber;
	}

	public void setViewedNumber(int viewedNumber) {
		this.viewedNumber = viewedNumber;
	}
	
}
