package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.QualificationRatingMethodDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QualificationRatingMethodDetailsRepository extends JpaRepository<QualificationRatingMethodDetails,Long> {
}
