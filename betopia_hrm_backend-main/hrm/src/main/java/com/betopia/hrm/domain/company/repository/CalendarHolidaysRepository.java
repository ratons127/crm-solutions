package com.betopia.hrm.domain.company.repository;

import com.betopia.hrm.domain.company.entity.CalendarHolidays;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CalendarHolidaysRepository extends JpaRepository<CalendarHolidays,Long> {
    @Query(
            value = "SELECT ch.* " +
                    "FROM calendar_holidays ch " +
                    "JOIN calendars c ON ch.calendar_id = c.id " +
                    "WHERE c.year = EXTRACT(YEAR FROM CURRENT_DATE)",
            nativeQuery = true
    )
    List<CalendarHolidays> findHolidaysForCurrentYear();


    @Modifying
    @Transactional
    @Query("UPDATE CalendarHolidays ch " +
            "SET ch.status = :status " +
            "WHERE ch.calendar.year = :year")
    void updateStatusByYear(int year, Boolean status);

    Optional<CalendarHolidays> findByHolidayDateAndIsHoliday(LocalDate date, Boolean isHoliday);
}
