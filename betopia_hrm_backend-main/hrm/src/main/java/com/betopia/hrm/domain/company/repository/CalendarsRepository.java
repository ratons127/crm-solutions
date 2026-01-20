package com.betopia.hrm.domain.company.repository;

import com.betopia.hrm.domain.company.entity.Calendars;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarsRepository extends JpaRepository<Calendars,Long> {
}
