package com.betopia.hrm.domain.company.repository;

import com.betopia.hrm.domain.company.entity.SeparationPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeparationPolicyRepository extends JpaRepository<SeparationPolicy, Long> {


    List<SeparationPolicy> findAllByCompanyId(Long companyId);
    boolean existsByCode(String code);

}
