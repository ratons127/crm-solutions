package com.betopia.hrm.services.leaves.leavebalanceemployee;

import com.betopia.hrm.domain.leave.entity.LeaveBalanceEmployee;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LeaveBalanceEmployeeService {

    List<LeaveBalanceEmployee> getBalancesForEmployee(Long employeeId, int year);

    void initializeYearlyBalances(Long employeeId, int year);

    boolean applyLeave(LeaveRequest request);

    void processYearEnd(Long employeeId, int year);

    void accrueLeave(Long employeeId, Long leaveTypeId, int year, Integer daysWorked);

    void deductLeaveBalance(LeaveRequest leaveRequest);

    void deleteLeaveBalance(Long id);

    List<LeaveBalanceEmployee> importAndSave(MultipartFile file) throws Exception;

}
