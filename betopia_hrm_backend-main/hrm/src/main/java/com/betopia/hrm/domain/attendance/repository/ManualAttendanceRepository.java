package com.betopia.hrm.domain.attendance.repository;

import com.betopia.hrm.domain.attendance.entity.ManualAttendance;
import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ManualAttendanceRepository extends JpaRepository<ManualAttendance,Long>, JpaSpecificationExecutor<ManualAttendance> {

    boolean existsByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);

}
