package com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine;


import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;

public interface LeaveRule {

    ValidationResult validate(Employee employee, LeaveRequest request, LeavePolicy policy);
}
