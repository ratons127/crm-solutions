package com.betopia.hrm.domain.dto.attendance.mapper;

import com.betopia.hrm.domain.attendance.entity.Shift;
import com.betopia.hrm.domain.attendance.entity.ShiftAssignments;
import com.betopia.hrm.domain.dto.attendance.ShiftDTO;
import com.betopia.hrm.domain.dto.attendance.ShowShiftAssignDTO;
import com.betopia.hrm.domain.dto.employee.EmployeeDTO;
import com.betopia.hrm.domain.dto.employee.ShowAssignedEmployeeDTO;
import com.betopia.hrm.domain.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShowShiftAssignDTOMapper {

    @Mapping(source = "employee", target = "employee", qualifiedByName = "toShowAssignedEmployeeDTO")
    @Mapping(source = "shift", target = "shift", qualifiedByName = "toShiftDTO")
    ShowShiftAssignDTO toDTO(ShiftAssignments assignment);

    @Named("toShowAssignedEmployeeDTO")
    default ShowAssignedEmployeeDTO toShowAssignedEmployeeDTO(Employee employee) {
        if (employee == null) return null;
        ShowAssignedEmployeeDTO dto = new ShowAssignedEmployeeDTO();
        dto.setId(employee.getId());
        dto.setEmployeeSerialId(employee.getEmployeeSerialId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        return dto;
    }

    @Named("toShiftDTO")
    default ShiftDTO toShiftDTO(Shift shift) {
        if (shift == null) return null;
        ShiftDTO dto = new ShiftDTO();
        dto.setId(shift.getId());
        dto.setShiftName(shift.getShiftName());
        return dto;
    }


}
