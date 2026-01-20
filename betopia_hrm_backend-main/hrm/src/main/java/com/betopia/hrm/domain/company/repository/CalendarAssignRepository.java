package com.betopia.hrm.domain.company.repository;

import com.betopia.hrm.domain.company.entity.CalendarAssign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarAssignRepository extends JpaRepository<CalendarAssign, Long> {
}
