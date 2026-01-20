package com.betopia.hrm.domain.dto.leave.mapper;


import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.dto.employee.EmployeeDTO;
import com.betopia.hrm.domain.dto.leave.ApprovalsShowDTO;
import com.betopia.hrm.domain.dto.leave.LeaveRequestDTO;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ApprovalsShowMapper {

    @Mapping(source = "leaveRequest", target = "leaveRequest", qualifiedByName = "toLeaveRequestDTO")
    @Mapping(source = "employee", target = "employee", qualifiedByName = "toEmployeeDTO")
    ApprovalsShowDTO toDTO(LeaveApprovals leaveApproval);

    // --- Leave Request ---
    @Named("toLeaveRequestDTO")
    default LeaveRequestDTO toLeaveRequestDTO(LeaveRequest leaveRequest) {
        if (leaveRequest == null) return null;
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(leaveRequest.getId());
        dto.setReason(leaveRequest.getReason());
        return dto;
    }

    // --- Employee ---
    @Named("toEmployeeDTO")
    default EmployeeDTO toEmployeeDTO(Employee employee) {
        if (employee == null) return null;
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        return dto;
    }

}
