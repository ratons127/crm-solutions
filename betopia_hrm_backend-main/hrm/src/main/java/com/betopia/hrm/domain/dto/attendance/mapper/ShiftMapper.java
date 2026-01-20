package com.betopia.hrm.domain.dto.attendance.mapper;

import com.betopia.hrm.domain.attendance.entity.Shift;
import com.betopia.hrm.domain.attendance.entity.ShiftCategory;
import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.dto.attendance.ShiftCategoryDTO;
import com.betopia.hrm.domain.dto.attendance.ShiftDTO;
import com.betopia.hrm.domain.dto.company.*;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import org.mapstruct.ReportingPolicy;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShiftMapper {

    //ShiftDTO toDTO(Shift shift);

    // List mapping
    //List<ShiftDTO> toDTOList(List<Shift> shifts);

    // Main mapping - Entity to DTO
    //@Mapping(source = "shiftCategory", target = "shiftCategory", qualifiedByName = "toShiftCategoryDTO")
   // @Mapping(source = "company", target = "company", qualifiedByName = "toCompanyDTO")
    @Mapping(source = "shiftCategory.id", target = "shiftCategoryId")
    @Mapping(source = "company.id", target = "companyId")
    ShiftDTO toDTO(Shift shift);

    // List mapping
    List<ShiftDTO> toDTOList(List<Shift> shifts);

    // Custom mappings for nested objects
    @Named("toShiftCategoryDTO")
    default ShiftCategoryDTO toShiftCategoryDTO(ShiftCategory shiftCategory) {
        if (shiftCategory == null) {
            return null;
        }
        ShiftCategoryDTO dto = new ShiftCategoryDTO();
        dto.setId(shiftCategory.getId());
        dto.setName(shiftCategory.getName());
        dto.setType(shiftCategory.getType());
        return dto;
    }

    @Named("toCompanyDTO")
    default CompanyDTO toCompanyDTO(Company company) {
        if (company == null) {
            return null;
        }
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setShortName(company.getShortName());
        dto.setCode(company.getCode());
        dto.setSlug(company.getSlug());
        return dto;
    }

    @Named("toBusinessUnitDTO")
    default BusinessUnitDTO toBusinessUnitDTO(BusinessUnit businessUnit) {
        if (businessUnit == null) {
            return null;
        }
        BusinessUnitDTO dto = new BusinessUnitDTO();
        dto.setId(businessUnit.getId());
        dto.setName(businessUnit.getName());
        dto.setCode(businessUnit.getCode());
        if (businessUnit.getCompany() != null) {
            dto.setCompanyId(businessUnit.getCompany().getId());
            dto.setCompanyName(businessUnit.getCompany().getName());
        }
        return dto;
    }

    @Named("toWorkplaceGroupDTO")
    default WorkplaceGroupDTO toWorkplaceGroupDTO(WorkplaceGroup workplaceGroup) {
        if (workplaceGroup == null) {
            return null;
        }
        WorkplaceGroupDTO dto = new WorkplaceGroupDTO();
        dto.setId(workplaceGroup.getId());
        dto.setName(workplaceGroup.getName());
        dto.setCode(workplaceGroup.getCode());
        if (workplaceGroup.getBusinessUnit() != null) {
            dto.setBusinessUnitId(workplaceGroup.getBusinessUnit().getId());
        }
        return dto;
    }

    @Named("toWorkplaceDTO")
    default WorkplaceDTO toWorkplaceDTO(Workplace workplace) {
        if (workplace == null) {
            return null;
        }
        WorkplaceDTO dto = new WorkplaceDTO();
        dto.setId(workplace.getId());
        dto.setName(workplace.getName());
        dto.setCode(workplace.getCode());
        dto.setAddress(workplace.getAddress());
        if (workplace.getWorkplaceGroup() != null) {
            dto.setWorkplaceGroupId(workplace.getWorkplaceGroup().getId());
        }
        return dto;
    }

    @Named("toDepartmentDTO")
    default DepartmentDTO toDepartmentDTO(Department department) {
        if (department == null) {
            return null;
        }
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        dto.setCode(department.getCode());
        dto.setDescription(department.getDescription());
        if (department.getWorkplace() != null) {
            dto.setWorkplaceId(department.getWorkplace().getId());
        }
        return dto;
    }

    @Named("toTeamDTO")
    default TeamDTO toTeamDTO(Team team) {
        if (team == null) {
            return null;
        }
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setName(team.getName());
        dto.setCode(team.getCode());
        dto.setDescription(team.getDescription());
        if (team.getDepartment() != null) {
            dto.setDepartmentId(team.getDepartment().getId());
        }
        return dto;
    }
}
