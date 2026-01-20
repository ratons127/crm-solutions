package com.betopia.hrm.domain.employee.request;


public record EmployeeShiftAssignRequest(

         Long employeeId,
         Long companyId
) {
}
