package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.HandoverChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandoverChecklistRepository extends JpaRepository<HandoverChecklist, Long> {
}
