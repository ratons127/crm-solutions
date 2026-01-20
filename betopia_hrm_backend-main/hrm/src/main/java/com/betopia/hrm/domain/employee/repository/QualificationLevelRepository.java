package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.QualificationLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualificationLevelRepository extends JpaRepository<QualificationLevel, Long> {
}
