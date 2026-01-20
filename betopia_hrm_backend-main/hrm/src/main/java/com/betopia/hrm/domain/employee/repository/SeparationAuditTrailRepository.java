package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.SeparationAuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeparationAuditTrailRepository extends JpaRepository<SeparationAuditTrail, Long> {
}
