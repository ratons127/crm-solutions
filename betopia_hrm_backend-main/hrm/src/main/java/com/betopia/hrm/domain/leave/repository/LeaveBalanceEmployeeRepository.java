package com.betopia.hrm.domain.leave.repository;

import com.betopia.hrm.domain.leave.entity.LeaveBalanceEmployee;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveBalanceEmployeeRepository extends JpaRepository<LeaveBalanceEmployee, Long> {

    List<LeaveBalanceEmployee> findByEmployeeIdAndYear(Long employeeId, int year);

    List<LeaveBalanceEmployee> findByYear(int year);

    Optional<LeaveBalanceEmployee> findByEmployeeIdAndLeaveTypeAndYear(Long employeeId, LeaveType leaveType, int year);

    Optional<LeaveBalanceEmployee> findByEmployeeIdAndLeaveTypeIdAndYear(Long employeeId, Long leaveTypeId, int year);
}
