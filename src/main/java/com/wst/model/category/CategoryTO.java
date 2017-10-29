package com.wst.model.category;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.wst.model.article.ArticleTO;
import com.wst.model.topic.TopicTO;
import com.wst.model.user.UserTO;

@Entity
@Table(name = "t_category")
public class CategoryTO {
	
	@Id
	@GeneratedValue
	@Column(name = "id", updatable = false, nullable = false)
	private Integer id;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "description")
	private String description;
	
	@OneToMany(mappedBy = "category", cascade = {CascadeType.PERSIST})
	private List<TopicTO> topics;
	
    @ManyToMany(mappedBy = "userCategories", cascade = {CascadeType.PERSIST})
    private Set<UserTO> users;

		
	public CategoryTO() {}
	
	public CategoryTO(Integer id, String name, String description) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	@Transient
	public ArticleTO getLatestArticle() {
		if(topics == null || topics.isEmpty()) {
			return null;
		} else {
			List<ArticleTO> tempList = new ArrayList<ArticleTO>();
			for(TopicTO topic : topics) {
				tempList.add(topic.getLatestArticle());
			}
			Collections.sort(tempList, Collections.reverseOrder());
			return tempList.get(0);
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
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public List<TopicTO> getTopics() {
		return topics;
	}

	public Set<UserTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserTO> users) {
        this.users = users;
    }
	
}
