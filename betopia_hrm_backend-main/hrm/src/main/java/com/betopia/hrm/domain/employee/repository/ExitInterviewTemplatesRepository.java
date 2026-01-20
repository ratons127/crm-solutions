package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.ExitInterviewTemplates;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExitInterviewTemplatesRepository extends JpaRepository<ExitInterviewTemplates, Long> {
}
