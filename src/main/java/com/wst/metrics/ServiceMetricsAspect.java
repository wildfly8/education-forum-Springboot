package com.wst.metrics;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.stereotype.Component;

import com.wst.model.topic.TopicService;
import com.wst.model.topic.TopicTO;


@Aspect
@Component
class GreetingServiceMetricsAspect {

    private final CounterService counterService;
    
	@Autowired
	private TopicService topicService;
    
    @Autowired
    public GreetingServiceMetricsAspect(CounterService counterService) {
        this.counterService = counterService;
    }

    
    @AfterReturning(pointcut = "execution(* com.wst.controller.ArticleController.getAllArticles(..)) && args(topicId,..)")
    public void afterCallingGetAllArticles(Integer topicId) {
        counterService.increment("counter.calls.getAllArticles." + topicId);
        TopicTO topic = topicService.getTopic(topicId);
        topic.setViewedNumber(topic.getViewedNumber() + 1);
        topicService.save(topic);
    }

    @AfterThrowing(pointcut = "execution(* com.wst.controller.ArticleController.getAllArticles(..))", throwing = "e")
    public void afterGetAllArticlesThrowsException(Exception e) {
        counterService.increment("counter.errors.getAllArticles");
    }

}