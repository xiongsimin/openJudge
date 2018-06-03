package com.openJudge.openJudge.domain.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.openJudge.openJudge.domain.entity.SupAdmin;

@Repository
public interface SupAdminRepository extends CrudRepository<SupAdmin, Long>{
	SupAdmin findSupAdminById(Long id);
}
