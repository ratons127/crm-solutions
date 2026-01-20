package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.QualificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualificationTypeRepository extends JpaRepository<QualificationType, Long> {
}
