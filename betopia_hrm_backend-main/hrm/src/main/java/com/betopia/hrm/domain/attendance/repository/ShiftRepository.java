package com.betopia.hrm.domain.attendance.repository;

import com.betopia.hrm.domain.attendance.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
}
