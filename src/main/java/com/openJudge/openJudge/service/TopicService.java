package com.openJudge.openJudge.service;

import com.openJudge.openJudge.entity.Topic;

/**
 * @Author aries
 * @Data 2021-02-18
 */
public interface TopicService {
    /*
     * 通过id查询题目
     */
    Topic findTopicById(Long id);
}
