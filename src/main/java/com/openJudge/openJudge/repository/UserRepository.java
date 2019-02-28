package com.openJudge.openJudge.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openJudge.openJudge.entity.User;
@Repository
public interface UserRepository extends CrudRepository<User,Long>{
	@Query(value="select * from user where account=?1",nativeQuery=true)
	User findUserByAccount(@Param("account") String account);
}
