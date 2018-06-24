package com.openJudge.openJudge.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openJudge.openJudge.entity.Topic;

@Repository
public interface TopicRepository extends CrudRepository<Topic,Long> {
	Topic findTopicById(Long id);
	@Query(value="select * from topic where competition_id=?1",nativeQuery=true)
	List<Topic> findTopicByCompetitionId(@Param("competition_id") Long competitionId);
	@Modifying
	@Transactional
	@Query(value="delete from topic where competition_id=?1",nativeQuery=true)
	void deleteByCompetitionId(@Param("competition_id") Long competitionId);
}
