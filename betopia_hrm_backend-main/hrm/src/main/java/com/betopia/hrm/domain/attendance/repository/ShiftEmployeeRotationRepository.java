package com.betopia.hrm.domain.attendance.repository;

import com.betopia.hrm.domain.attendance.entity.ShiftEmployeeRotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftEmployeeRotationRepository extends JpaRepository<ShiftEmployeeRotation, Long> {
}
