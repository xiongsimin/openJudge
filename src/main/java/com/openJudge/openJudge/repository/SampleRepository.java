package com.openJudge.openJudge.repository;

import com.openJudge.openJudge.entity.Record;
import com.openJudge.openJudge.entity.Sample;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author aries
 * @Data 2021-02-10
 */
@Repository
public interface SampleRepository extends CrudRepository<Sample, Long> {
    /**
     * 根据题目查询测试用例
     *
     * @param topicId
     * @return
     */
    List<Sample> findSampleByTopicId(@Param("topic_id") Long topicId);
}
