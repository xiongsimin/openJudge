package com.openJudge.openJudge.service.Impl;

import com.openJudge.openJudge.entity.Topic;
import com.openJudge.openJudge.repository.TopicRepository;
import com.openJudge.openJudge.service.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * @Author aries
 * @Data 2021-02-18
 */
@Service("topicService")
public class TopicServiceImpl implements TopicService {
    @Autowired
    private TopicRepository topicRepository;
    @Override
    @Cacheable(value = "TopicCache", key = "'findTopicById['+#id+']'")
    public Topic findTopicById(Long id) {
        return topicRepository.findTopicById(id);
    }
}
