package com.openJudge.openJudge.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openJudge.openJudge.domain.entity.Topic;

@Repository
public interface TopicRepository extends CrudRepository<Topic,Long> {
//	@Query(value="select * from topic",nativeQuery=true)
//	List<Topic,Competition> findTopic();
}
