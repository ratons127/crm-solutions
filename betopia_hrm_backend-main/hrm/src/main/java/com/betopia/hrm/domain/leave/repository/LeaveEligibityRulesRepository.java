package com.betopia.hrm.domain.leave.repository;

import com.betopia.hrm.domain.leave.entity.LeaveEligibilityRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveEligibityRulesRepository extends JpaRepository<LeaveEligibilityRules, Long> {
}
