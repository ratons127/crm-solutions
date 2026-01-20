package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.QualificationRatingMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualificationRatingMethodRepository extends JpaRepository<QualificationRatingMethod, Long> {
}
