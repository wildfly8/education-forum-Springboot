package com.gslf.model.topic;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TopicService {
	
	@Autowired
	private TopicRepository topicRepository;
	
	
	public List<TopicTO> getAllTopics() {
		List<TopicTO> topics = new ArrayList<TopicTO>();
		topicRepository.findAll().forEach(topics::add);
		return topics;
	}
	
	public TopicTO getTopic(Integer id) {
		return topicRepository.findOne(id);
	}
	
	public void save(TopicTO topic) {
		topicRepository.save(topic);
	}
	
	public void updateTopic(TopicTO topic) {
		topicRepository.save(topic);
	}
	
	public void deleteTopic(Integer id) {
		topicRepository.delete(id);
	}

}