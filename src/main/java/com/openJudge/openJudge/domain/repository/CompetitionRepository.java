package com.openJudge.openJudge.domain.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openJudge.openJudge.domain.entity.Competition;

@Repository
public interface CompetitionRepository extends CrudRepository<Competition, Long> {
	Competition findCompetitionById(Long id);
//	Iterable<Competition> findCompetitionByState(int state);
	/*
	 * 查询未发布的比赛
	 */
	@Query(value="select * from competition where state=0 AND type=0",nativeQuery=true)
	public List<Competition> findDisPublishCompetition();
	/*
	 * 查询已发布但未开始的比赛
	 */
	@Query(value="select * from competition where state=1 AND start_time>?1 AND type=0",nativeQuery=true)
	public List<Competition> findDisStartCompetition(@Param("start_time") Timestamp time);
	/*
	 * 查询正在进行的比赛（已发布）
	 */
	@Query(value="select * from competition where state=1 AND start_time<?1 AND end_time>=?1 AND type=0",nativeQuery=true)
	List<Competition> findBeginningCompetition(@Param("stare_time") Timestamp time);
	/*
	 * 查询已结束的比赛
	 */
	@Query(value="select * from competition where state=1 AND end_time<?1 AND type=0",nativeQuery=true)
	List<Competition> findFinishCompetition(Timestamp time);
	/*
	 * ***********************************************************
	 * ************************分隔线*****************************
	 * ***********************************************************
	 */
	/*
	 * 查询未发布的训练
	 */
	@Query(value="select * from competition where state=0 AND type=1",nativeQuery=true)
	List<Competition> findDisPublishPractice();
	/*
	 * 查询已发布的训练
	 */
	@Query(value="select * from competition where state=1 AND type=1",nativeQuery=true)
	List<Competition> findPublishedPractice();
}
