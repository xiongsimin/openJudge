package com.openJudge.openJudge.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openJudge.openJudge.entity.Competition;
import com.openJudge.openJudge.entity.Topic;

@Repository
public interface CompetitionRepository extends CrudRepository<Competition, Long> {
	/**
	 * ***********************************************************
	 * ************************比赛*******************************
	 * ***********************************************************
	 */
	/**
	 * 查询未发布的比赛
	 */
	@Query(value="select * from competition where state=0 AND type=0",nativeQuery=true)
	public List<Competition> findDisPublishCompetition();
	/**
	 * 查询已发布但未开始的比赛
	 */
	@Query(value="select * from competition where state=1 AND start_time>?1 AND type=0",nativeQuery=true)
	public List<Competition> findDisStartCompetition(@Param("start_time") Timestamp time);
	/**
	 * 查询正在进行的比赛（已发布）
	 */
	@Query(value="select * from competition where state=1 AND start_time<?1 AND end_time>=?1 AND type=0",nativeQuery=true)
	List<Competition> findBeginningCompetition(@Param("stare_time") Timestamp time);
	/**
	 * 查询已结束的比赛
	 */
	@Query(value="select * from competition where state=1 AND end_time<?1 AND type=0",nativeQuery=true)
	List<Competition> findFinishCompetition(@Param("end_time") Timestamp time);
	/**
	 * ***********************************************************
	 * ************************练习*****************************
	 * ***********************************************************
	 */
	/**
	 * 查询未发布的练习
	 */
	@Query(value="select * from competition where state=0 AND type=1",nativeQuery=true)
	List<Competition> findDisPublishPractice();
	/**
	 * 查询已发布的练习
	 */
	@Query(value="select * from competition where state=1 AND type=1",nativeQuery=true)
	List<Competition> findPublishedPractice();
	/**
	 * 查找所有练习
	 */
	@Query(value="select * from competition where type=1",nativeQuery=true)
	List<Competition> findAllPractice();
	/*
	 * ***********************************************************
	 * ***********************公用********************************
	 * ***********************************************************
	 */
	/**
	 * 通过title查找比赛（练习）
	 */
	@Query(value="select * from competition where title=?1",nativeQuery=true)
	Competition findCompetitionByTitle(@Param("title") String title);
	/**
	 * 查找未发布的比赛或练习
	 */
	@Query(value="select * from competition where state=0",nativeQuery=true)
	List<Competition> findDisPublishCompetitionOrPractice();
	/**
	 * 通过id查询比赛（实际也包括练习）
	 * @param id 比赛的id
	 */
	Competition findCompetitionById(Long id);
}
