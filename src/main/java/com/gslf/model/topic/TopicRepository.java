package com.gslf.model.topic;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface TopicRepository extends CrudRepository<TopicTO, Integer> {

}
