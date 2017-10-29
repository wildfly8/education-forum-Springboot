package com.gslf.model.article;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gslf.model.user.UserTO;

@Repository
@Transactional
public interface ArticleRepository extends CrudRepository<ArticleTO, Integer> {
	
	Integer countByAuthor(UserTO author);

}
