package com.betopia.hrm.domain.leave.repository;

import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long>, JpaSpecificationExecutor<LeaveRequest> {

    @Query("SELECT lr FROM LeaveRequest lr " +
            "WHERE lr.employeeId = :employeeId " +
            "AND lr.status = com.betopia.hrm.domain.leave.enums.LeaveStatus.APPROVED " +
            "AND lr.startDate <= :endDate " +
            "AND lr.endDate >= :startDate")
    List<LeaveRequest> findOverlaps(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // if editing an existing request, exclude that ID
    @Query("SELECT lr FROM LeaveRequest lr " +
            "WHERE lr.employeeId = :employeeId " +
            "AND lr.status = com.betopia.hrm.domain.leave.enums.LeaveStatus.APPROVED " +
            "AND lr.id <> :excludeId " +
            "AND lr.startDate <= :endDate " +
            "AND lr.endDate >= :startDate")
    List<LeaveRequest> findOverlapsExcludingSelf(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("excludeId") Long excludeId
    );

    boolean existsByEmployeeIdAndStartDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}
