package com.openJudge.openJudge.service.Impl;

import com.openJudge.openJudge.entity.Sample;
import com.openJudge.openJudge.repository.SampleRepository;
import com.openJudge.openJudge.service.SampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author aries
 * @Data 2021-02-10
 */
@Service("sampleService")
public class SampleServiceImpl implements SampleService {
    @Autowired
    SampleRepository sampleRepository;

    //value指定缓存位置名称
    @Override
    @Cacheable(value = "SamplesCache", key = "'findSampleByTopicId['+#topicId+']'")
    public List<Sample> findSampleByTopicId(Long topicId) {
        return sampleRepository.findSampleByTopicId(topicId);
    }
}
