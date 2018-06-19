package com.openJudge.openJudge.domain.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.openJudge.openJudge.domain.entity.SupAdmin;

@Repository
public interface SupAdminRepository extends CrudRepository<SupAdmin, Long>{
	SupAdmin findSupAdminById(Long id);
	/*@Query(value="select * from sup_admin where id=:id",nativeQuery=true)
	public SupAdmin nativeQuery(@Param("id") Long id);*/
}
