package com.betopia.hrm.domain.dto.hierarchyfilter.mapper;

import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.dto.company.*;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HierarchyFilterMapper {

    @Named("toCompanyDTO")
    default CompanyDTO toCompanyDTO(Company company) {
        if (company == null) return null;
        CompanyDTO dto = new CompanyDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        return dto;
    }

    @Named("toBusinessUnitDTO")
    default BusinessUnitDTO toBusinessUnitDTO(BusinessUnit businessUnit) {
        if (businessUnit == null) return null;
        BusinessUnitDTO dto = new BusinessUnitDTO();
        dto.setId(businessUnit.getId());
        dto.setName(businessUnit.getName());
        return dto;
    }

    @Named("toWorkplaceGroupDTO")
    default WorkplaceGroupDTO toWorkplaceGroupDTO(WorkplaceGroup workplaceGroup) {
        if (workplaceGroup == null) return null;
        WorkplaceGroupDTO dto = new WorkplaceGroupDTO();
        dto.setId(workplaceGroup.getId());
        dto.setName(workplaceGroup.getName());
        return dto;
    }

    @Named("toWorkplaceDTO")
    default WorkplaceDTO toWorkplaceDTO(Workplace workplace) {
        if (workplace == null) return null;
        WorkplaceDTO dto = new WorkplaceDTO();
        dto.setId(workplace.getId());
        dto.setName(workplace.getName());
        return dto;
    }

    @Named("toDepartmentDTO")
    default DepartmentDTO toDepartmentDTO(Department department) {
        if (department == null) return null;
        DepartmentDTO dto = new DepartmentDTO();
        dto.setId(department.getId());
        dto.setName(department.getName());
        return dto;
    }

    @Named("toTeamDTO")
    default TeamDTO toTeamDTO(Team team) {
        if (team == null) return null;
        TeamDTO dto = new TeamDTO();
        dto.setId(team.getId());
        dto.setName(team.getName());
        return dto;
    }
    default List<CompanyDTO> toCompanyDTOList(List<Company> companies) {
        if (companies == null) return null;
        return companies.stream()
                .map(this::toCompanyDTO)
                .collect(Collectors.toList());
    }

    default List<BusinessUnitDTO> toBusinessUnitDTOList(List<BusinessUnit> businessUnits) {
        if (businessUnits == null) return null;
        return businessUnits.stream()
                .map(this::toBusinessUnitDTO)
                .collect(Collectors.toList());
    }

    default List<WorkplaceGroupDTO> toWorkplaceGroupDTOList(List<WorkplaceGroup> workplaceGroups) {
        if (workplaceGroups == null) return null;
        return workplaceGroups.stream()
                .map(this::toWorkplaceGroupDTO)
                .collect(Collectors.toList());
    }

    default List<WorkplaceDTO> toWorkplaceDTOList(List<Workplace> workplaces) {
        if (workplaces == null) return null;
        return workplaces.stream()
                .map(this::toWorkplaceDTO)
                .collect(Collectors.toList());
    }

    default List<DepartmentDTO> toDepartmentDTOList(List<Department> departments) {
        if (departments == null) return null;
        return departments.stream()
                .map(this::toDepartmentDTO)
                .collect(Collectors.toList());
    }

    default List<TeamDTO> toTeamDTOList(List<Team> teams) {
        if (teams == null) return null;
        return teams.stream()
                .map(this::toTeamDTO)
                .collect(Collectors.toList());
    }
}
