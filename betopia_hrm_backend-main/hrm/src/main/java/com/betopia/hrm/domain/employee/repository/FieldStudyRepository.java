package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.FieldStudy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FieldStudyRepository extends JpaRepository<FieldStudy, Long> {
}
