package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.SeparationApprovals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeparationApprovalsRepository extends JpaRepository<SeparationApprovals, Long> {
}
