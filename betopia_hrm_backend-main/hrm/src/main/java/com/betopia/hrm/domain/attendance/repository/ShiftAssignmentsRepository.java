package com.betopia.hrm.domain.attendance.repository;

import com.betopia.hrm.domain.attendance.entity.AttendanceApproval;
import com.betopia.hrm.domain.attendance.entity.ShiftAssignments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ShiftAssignmentsRepository extends JpaRepository<ShiftAssignments,Long>,
        JpaSpecificationExecutor<ShiftAssignments> {

    @Query("""
    SELECT sa FROM ShiftAssignments sa
    WHERE sa.employee.id = :employeeId
      AND (sa.effectiveTo IS NULL OR sa.effectiveTo >= CURRENT_DATE)
    ORDER BY sa.effectiveFrom DESC
    LIMIT 1
""")
    Optional<ShiftAssignments> findActiveShiftByEmployeeId(@Param("employeeId") Long employeeId);

    List<ShiftAssignments> findByAssignedBy(Long assignedBy);

    @Query("""
    SELECT sa FROM ShiftAssignments sa
    WHERE sa.employee.id = :employeeId
      AND sa.effectiveFrom <= :newEffectiveTo
      AND (sa.effectiveTo IS NULL OR sa.effectiveTo >= :newEffectiveFrom)
""")
    List<ShiftAssignments> findOverlappingAssignments(
            @Param("employeeId") Long employeeId,
            @Param("newEffectiveFrom") LocalDate newEffectiveFrom,
            @Param("newEffectiveTo") LocalDate newEffectiveTo
    );

    Optional<ShiftAssignments> findTopByEmployeeIdOrderByEffectiveToDesc(Long employeeId);



}
