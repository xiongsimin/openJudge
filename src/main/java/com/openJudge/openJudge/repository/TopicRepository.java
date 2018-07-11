package com.openJudge.openJudge.repository;

import java.sql.ResultSet;
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
	/*
	 * 通过id查询题目
	 */
	Topic findTopicById(Long id);
	/*
	 * 通过competition_id查询题目
	 */
	@Query(value="select * from topic t,competition c where competition_id=?1 and t.competition_id=c.id order by t.id",nativeQuery=true)
	List<Topic> findTopicByCompetitionId(@Param("competition_id") Long competitionId);
	/*
	 * 通过competition_id删除题目（原子操作）
	 */
	@Modifying
	@Transactional
	@Query(value="delete from topic where competition_id=?1",nativeQuery=true)
	void deleteByCompetitionId(@Param("competition_id") Long competitionId);
	@Query(value="select distinct * from topic t,competition c where t.competition_id=c.id and c.type=1",nativeQuery=true)
	List<Topic> findTopicOfPractice();
}
