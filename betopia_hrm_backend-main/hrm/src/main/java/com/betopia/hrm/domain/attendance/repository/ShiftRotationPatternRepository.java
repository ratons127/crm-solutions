package com.betopia.hrm.domain.attendance.repository;

import com.betopia.hrm.domain.attendance.entity.ShiftRotationPatterns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRotationPatternRepository extends JpaRepository<ShiftRotationPatterns, Long> {
}
