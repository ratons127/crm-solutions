package com.betopia.hrm.domain.dto.leave.mapper;

import com.betopia.hrm.domain.dto.employee.EmployeeDTO;
import com.betopia.hrm.domain.dto.leave.LeaveApprovalsDTO;
import com.betopia.hrm.domain.dto.leave.LeaveRequestDTO;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LeaveApprovalsMapper {

    @Mapping(source = "employee", target = "employee", qualifiedByName = "toEmployeeDTO")
    LeaveApprovalsDTO toDTO(LeaveApprovals leaveApproval);

    // List mapping
    List<LeaveApprovalsDTO> toDTOList(List<LeaveApprovals> leaveApprovals);

    // --- Employee ---
    @Named("toEmployeeDTO")
    default EmployeeDTO toEmployeeDTO(Employee employee) {
        if (employee == null) return null;
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setDesignationName(employee.getDesignation().getName());
        dto.setEmployeeSerialId(employee.getEmployeeSerialId());
        return dto;
    }

}
