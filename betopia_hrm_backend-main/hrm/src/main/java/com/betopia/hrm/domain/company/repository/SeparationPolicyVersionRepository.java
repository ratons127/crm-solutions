package com.betopia.hrm.domain.company.repository;

import com.betopia.hrm.domain.company.entity.SeparationPolicy;
import com.betopia.hrm.domain.company.entity.SeparationPolicyVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SeparationPolicyVersionRepository extends JpaRepository<SeparationPolicyVersion, Long> {

    List<SeparationPolicyVersion> findBySeparationPolicyOrderByIdDesc(SeparationPolicy separationPolicy);

}
