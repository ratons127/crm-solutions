package com.betopia.hrm.domain.leave.repository;

import com.betopia.hrm.domain.leave.entity.LeaveYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface LeaveYearRepository extends JpaRepository<LeaveYear, Long> {

    @Query("""
    SELECT YEAR(l.startDate)
    FROM LeaveYear l
    WHERE l.startDate <= :currentDate
    ORDER BY l.startDate DESC
""")
    Optional<Integer> findCurrentLeaveYear(@Param("currentDate") LocalDate currentDate);


}
