package com.openJudge.openJudge.service;

import com.openJudge.openJudge.entity.Sample;

import java.util.List;

/**
 * @Author aries
 * @Data 2021-02-10
 */
public interface SampleService {
    List<Sample> findSampleByTopicId(Long topicId);
}
