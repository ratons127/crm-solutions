package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.ExitClearanceTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExitClearanceTemplateRepository extends JpaRepository<ExitClearanceTemplate, Long> {
}
