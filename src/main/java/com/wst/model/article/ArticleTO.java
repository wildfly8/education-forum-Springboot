package com.wst.model.article;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.wst.model.topic.TopicTO;
import com.wst.model.user.UserTO;

@Entity
@Table(name="t_article")
public class ArticleTO implements Comparable<ArticleTO> {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@NotNull
	private String title;
	
	@Lob
	private String content;
	
	private int grade;
	private Date createDate;
	private Date lastUpdate;
	
	@ManyToOne
	@JoinColumn(name = "topic_id")
	private TopicTO topic;

	@ManyToOne
	@JoinColumn(name = "author_id")
	private UserTO author;
	

	public ArticleTO() {}
	
	public ArticleTO(Integer id, String title, String content, int grade, Date createDate, Date lastUpdate, TopicTO topic, UserTO author) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.grade = grade;
		this.createDate = createDate;
		this.lastUpdate = lastUpdate;
		this.topic = topic;
		this.author = author;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getDescription() {
		return content;
	}
	
	public void setDescription(String description) {
		this.content = description;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getGrade() {
		return grade;
	}

	public void setGrade(int grade) {
		this.grade = grade;
	}
	
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	public TopicTO getTopic() {
		return topic;
	}

	public void setTopic(TopicTO topic) {
		this.topic = topic;
	}
	
	public UserTO getAuthor() {
		return author;
	}

	public void setAuthor(UserTO author) {
		this.author = author;
	}

	@Override
	public int compareTo(ArticleTO anotherArticle) {
		return this.lastUpdate.compareTo(anotherArticle.getLastUpdate());
	}

}
