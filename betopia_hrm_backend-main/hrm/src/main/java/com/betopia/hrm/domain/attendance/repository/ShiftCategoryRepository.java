package com.betopia.hrm.domain.attendance.repository;

import com.betopia.hrm.domain.attendance.entity.ShiftCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftCategoryRepository extends JpaRepository<ShiftCategory, Long> {
}
