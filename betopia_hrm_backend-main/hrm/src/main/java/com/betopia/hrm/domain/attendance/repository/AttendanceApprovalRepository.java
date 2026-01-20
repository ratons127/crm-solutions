package com.betopia.hrm.domain.attendance.repository;

import com.betopia.hrm.domain.attendance.entity.AttendanceApproval;
import com.betopia.hrm.domain.attendance.entity.ManualAttendance;
import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceApprovalRepository extends JpaRepository<AttendanceApproval, Long>, JpaSpecificationExecutor<AttendanceApproval> {

    List<AttendanceApproval> findByApproverId(Long approverId);
}
