package com.openJudge.openJudge.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openJudge.openJudge.entity.Record;

@Repository
public interface RecordRepository extends CrudRepository<Record,Long>{
	@Query(value="select * from record where user_id=?1",nativeQuery=true)
	List<Record> findRecordByUserId(@Param("user_id") Long userId);
}
