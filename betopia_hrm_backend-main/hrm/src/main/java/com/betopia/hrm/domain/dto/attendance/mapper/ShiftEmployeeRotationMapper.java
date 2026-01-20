package com.betopia.hrm.domain.dto.attendance.mapper;

import com.betopia.hrm.domain.attendance.entity.ShiftEmployeeRotation;
import com.betopia.hrm.domain.attendance.entity.ShiftRotationPatterns;
import com.betopia.hrm.domain.dto.attendance.ShiftEmployeeRotationDTO;
import com.betopia.hrm.domain.dto.attendance.ShiftRotationPatternDTO;
import com.betopia.hrm.domain.dto.attendance.ShiftRotationPatternDetailDTO;
import com.betopia.hrm.domain.dto.employee.EmployeeDTO;
import com.betopia.hrm.domain.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShiftEmployeeRotationMapper {

    // Main mapping - Entity to DTO
    @Mapping(source = "employee", target = "employee", qualifiedByName = "toEmployeeDTO")
    @Mapping(source = "pattern", target = "pattern", qualifiedByName = "toPatternDTO")
    ShiftEmployeeRotationDTO toDTO(ShiftEmployeeRotation shiftEmployeeRotation);

    // List mapping
    List<ShiftEmployeeRotationDTO> toDTOList(List<ShiftEmployeeRotation> shiftEmployeeRotation);

    // Custom mapping methods
    @Named("toEmployeeDTO")
    default EmployeeDTO toEmployeeDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setGender(employee.getGender());
        dto.setPresentAddress(employee.getPresentAddress());

        return dto;
    }

    @Named("toPatternDTO")
    default ShiftRotationPatternDTO toPatternDTO(ShiftRotationPatterns pattern) {
        if (pattern == null) {
            return null;
        }

        ShiftRotationPatternDTO dto = new ShiftRotationPatternDTO();

        dto.setId(pattern.getId());
        dto.setPatternName(pattern.getPatternName());
        dto.setRotationDays(pattern.getRotationDays());
        dto.setStatus(pattern.getStatus());

        return dto;
    }
}
