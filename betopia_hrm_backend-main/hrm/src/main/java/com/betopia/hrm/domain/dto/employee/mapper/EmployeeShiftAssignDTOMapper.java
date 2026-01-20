package com.betopia.hrm.domain.dto.employee.mapper;


import com.betopia.hrm.domain.attendance.entity.Shift;
import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.dto.attendance.ShiftDTO;
import com.betopia.hrm.domain.dto.company.BusinessUnitDTO;
import com.betopia.hrm.domain.dto.company.CompanyDTO;
import com.betopia.hrm.domain.dto.company.DepartmentDTO;
import com.betopia.hrm.domain.dto.company.TeamDTO;
import com.betopia.hrm.domain.dto.company.WorkplaceDTO;
import com.betopia.hrm.domain.dto.company.WorkplaceGroupDTO;
import com.betopia.hrm.domain.dto.employee.EmployeeShiftAssignDTO;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeShiftAssignDTOMapper {

    @Mapping(source = "company", target = "company", qualifiedByName = "toCompanyDTO")
    @Mapping(source = "shift", target = "shift", qualifiedByName = "toShiftDTO")
    EmployeeShiftAssignDTO toDTO(Employee employee);

    // --- Company ---
    @Named("toCompanyDTO")
    default CompanyDTO toCompanyDTO(Company company) {
        if (company == null) return null;
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        return dto;
    }



    // --- Shift ---
    @Named("toShiftDTO")
    default ShiftDTO toShiftDTO(Shift shift) {
        if (shift == null) return null;
        ShiftDTO dto = new ShiftDTO();
        dto.setId(shift.getId());
        dto.setShiftName(shift.getShiftName());
        dto.setStartTime(shift.getStartTime());
        return dto;
    }


}
