package com.betopia.hrm.domain.leave.repository;


import com.betopia.hrm.domain.leave.entity.LeaveTypeRules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveTypeRulesRepository extends JpaRepository<LeaveTypeRules, Long> {
}
