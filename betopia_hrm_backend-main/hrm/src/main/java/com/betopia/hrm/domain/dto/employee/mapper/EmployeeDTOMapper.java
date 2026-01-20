package com.betopia.hrm.domain.dto.employee.mapper;

import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.dto.company.BusinessUnitDTO;
import com.betopia.hrm.domain.dto.company.CompanyDTO;
import com.betopia.hrm.domain.dto.company.DepartmentDTO;
import com.betopia.hrm.domain.dto.company.TeamDTO;
import com.betopia.hrm.domain.dto.company.WorkplaceDTO;
import com.betopia.hrm.domain.dto.company.WorkplaceGroupDTO;
import com.betopia.hrm.domain.dto.employee.DesignationDTO;
import com.betopia.hrm.domain.dto.employee.EmployeeDTO;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.lookup.entity.LookupSetupEntry; // Don't forget this import if used in toLookupDetailsDTO
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmployeeDTOMapper {

    @Mapping(source = "employeeType.id",   target = "employeeTypeId")
    @Mapping(source = "department",     target = "department", qualifiedByName = "toDepartmentDTO")
    @Mapping(source = "designation",    target = "designation", qualifiedByName = "toDesignationDTO")
    @Mapping(source = "workplace",      target = "workplace", qualifiedByName = "toWorkplaceDTO")
    @Mapping(source = "businessUnit",   target = "businessUnit", qualifiedByName = "toBusinessUnitDTO")
    @Mapping(source = "workplaceGroup", target = "workplaceGroup", qualifiedByName = "toWorkplaceGroupDTO")
    @Mapping(source = "grade.id",          target = "gradeId")
    @Mapping(source = "company",        target = "company", qualifiedByName = "toCompanyDTO")
    @Mapping(source = "team",           target = "team", qualifiedByName = "toTeamDTO")
    @Mapping(target = "grossSalary", source = "grossSalary")
    @Mapping(source = "religion", target = "religionId")
    @Mapping(source = "nationality", target = "nationalityId")
    @Mapping(source = "bloodGroup", target = "bloodGroupId")
    @Mapping(source = "paymentType", target = "paymentTypeId")
    @Mapping(source = "probationDuration", target = "probationDurationId")
    @Mapping(target = "displayName", expression = "java(concatenateNames(employee.getFirstName(), employee.getLastName()))")
    @Mapping(source = "photo", target = "photo")
    @Mapping(source = "imageUrl", target = "imageUrl")
    EmployeeDTO toDTO(Employee employee);
    List<EmployeeDTO> toDTOList(List<Employee> employees);

    default String concatenateNames(String firstName, String lastName) {
        StringBuilder displayNameBuilder = new StringBuilder();
        if (firstName != null && !firstName.trim().isEmpty()) {
            displayNameBuilder.append(firstName.trim());
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            if (displayNameBuilder.length() > 0) {
                displayNameBuilder.append(" ");
            }
            displayNameBuilder.append(lastName.trim());
        }
        return displayNameBuilder.length() > 0 ? displayNameBuilder.toString() : null;
    }

    // --- Company ---
    @Named("toCompanyDTO")
    default CompanyDTO toCompanyDTO(Company company) {
        if (company == null) return null;
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        return dto;
    }

    // --- BusinessUnit ---
    @Named("toBusinessUnitDTO")
    default BusinessUnitDTO toBusinessUnitDTO(BusinessUnit bu) {
        if (bu == null) return null;
        BusinessUnitDTO dto = new BusinessUnitDTO();
        dto.setId(bu.getId());
        dto.setName(bu.getName());
        return dto;
    }

    // --- WorkplaceGroup ---
    @Named("toWorkplaceGroupDTO")
    default WorkplaceGroupDTO toWorkplaceGroupDTO(WorkplaceGroup wg) {
        if (wg == null) return null;
        WorkplaceGroupDTO dto = new WorkplaceGroupDTO();
        dto.setId(wg.getId());
        dto.setName(wg.getName());
        return dto;
    }

    // --- Workplace ---
    @Named("toWorkplaceDTO")
    default WorkplaceDTO toWorkplaceDTO(Workplace wp) {
        if (wp == null) return null;
        WorkplaceDTO dto = new WorkplaceDTO();
        dto.setId(wp.getId());
        dto.setName(wp.getName());
        return dto;
    }

    // --- Department ---
    @Named("toDepartmentDTO")
    default DepartmentDTO toDepartmentDTO(Department dept) {
        if (dept == null) return null;
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(dept.getId());
        dto.setName(dept.getName());
        return dto;
    }

    // --- Team ---
    @Named("toTeamDTO")
    default TeamDTO toTeamDTO(Team team) {
        if (team == null) return null;
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setName(team.getName());
        return dto;
    }

    // --- Designation ---
    @Named("toDesignationDTO")
    default DesignationDTO toDesignationDTO(Designation designation) {
        if (designation == null) return null;
        DesignationDTO dto = new DesignationDTO();
        dto.setId(designation.getId());
        dto.setName(designation.getName());
        return dto;
    }


}
