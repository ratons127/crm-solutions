package com.betopia.hrm.domain.dto.leave.mapper;

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
import com.betopia.hrm.domain.dto.leave.LeaveGroupAssignDTO;
import com.betopia.hrm.domain.dto.leave.LeaveGroupDTO;
import com.betopia.hrm.domain.dto.leave.LeaveTypeDTO;
import com.betopia.hrm.domain.leave.entity.LeaveGroup;
import com.betopia.hrm.domain.leave.entity.LeaveGroupAssign;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LeaveGroupAssignMapper {

    @Mapping(source = "leaveType", target = "leaveType", qualifiedByName = "toLeaveTypeDTO")
    @Mapping(source = "leaveGroup", target = "leaveGroup", qualifiedByName = "toLeaveGroupDTO")
    @Mapping(source = "company", target = "company", qualifiedByName = "toCompanyDTO")
    @Mapping(source = "businessUnit", target = "businessUnit", qualifiedByName = "toBusinessUnitDTO")
    @Mapping(source = "workplaceGroup", target = "workplaceGroup", qualifiedByName = "toWorkplaceGroupDTO")
    @Mapping(source = "workplace", target = "workplace", qualifiedByName = "toWorkplaceDTO")
    @Mapping(source = "department", target = "department", qualifiedByName = "toDepartmentDTO")
    @Mapping(source = "team", target = "team", qualifiedByName = "toTeamDTO")
    LeaveGroupAssignDTO toDTO(LeaveGroupAssign entity);

    @Named("toLeaveTypeDTO")
    default LeaveTypeDTO toLeaveTypeDTO(LeaveType leaveType) {
        if (leaveType == null) {
            return null;
        }
        LeaveTypeDTO dto = new LeaveTypeDTO();
        dto.setId(leaveType.getId());
        dto.setName(leaveType.getName());
        return dto;
    }

    @Named("toLeaveGroupDTO")
    default LeaveGroupDTO toLeaveGroupDTO(LeaveGroup leaveGroup) {
        if (leaveGroup == null) {
            return null;
        }
        LeaveGroupDTO dto = new LeaveGroupDTO();
        dto.setId(leaveGroup.getId());
        dto.setName(leaveGroup.getName());
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
