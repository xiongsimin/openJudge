package com.openJudge.openJudge.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openJudge.openJudge.entity.Topic;

@Repository
public interface TopicRepository extends CrudRepository<Topic,Long> {
//	@Query(value="select * from topic",nativeQuery=true)
//	List<Topic,Competition> findTopic();
}
