package com.betopia.hrm.domain.dto.hierarchyfilter;

import com.betopia.hrm.domain.dto.company.BusinessUnitDTO;
import com.betopia.hrm.domain.dto.company.CompanyDTO;
import com.betopia.hrm.domain.dto.company.DepartmentDTO;
import com.betopia.hrm.domain.dto.company.TeamDTO;
import com.betopia.hrm.domain.dto.company.WorkplaceDTO;
import com.betopia.hrm.domain.dto.company.WorkplaceGroupDTO;

import java.util.List;

public class HierarchyFilterDTO {

    private List<CompanyDTO> companies;
    private List<BusinessUnitDTO> businessUnits;
    private List<WorkplaceGroupDTO> workplaceGroups;
    private List<WorkplaceDTO> workplaces;
    private List<DepartmentDTO> departments;
    private List<TeamDTO> teams;

    public HierarchyFilterDTO(List<CompanyDTO> companies,
                              List<BusinessUnitDTO> businessUnits,
                              List<WorkplaceGroupDTO> workplaceGroups,
                              List<WorkplaceDTO> workplaces,
                              List<DepartmentDTO> departments,
                              List<TeamDTO> teams) {
        this.companies = companies;
        this.businessUnits = businessUnits;
        this.workplaceGroups = workplaceGroups;
        this.workplaces = workplaces;
        this.departments = departments;
        this.teams = teams;
    }

    public List<CompanyDTO> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyDTO> companies) {
        this.companies = companies;
    }

    public List<BusinessUnitDTO> getBusinessUnits() {
        return businessUnits;
    }

    public void setBusinessUnits(List<BusinessUnitDTO> businessUnits) {
        this.businessUnits = businessUnits;
    }

    public List<WorkplaceGroupDTO> getWorkplaceGroups() {
        return workplaceGroups;
    }

    public void setWorkplaceGroups(List<WorkplaceGroupDTO> workplaceGroups) {
        this.workplaceGroups = workplaceGroups;
    }

    public List<WorkplaceDTO> getWorkplaces() {
        return workplaces;
    }

    public void setWorkplaces(List<WorkplaceDTO> workplaces) {
        this.workplaces = workplaces;
    }

    public List<DepartmentDTO> getDepartments() {
        return departments;
    }

    public void setDepartments(List<DepartmentDTO> departments) {
        this.departments = departments;
    }

    public List<TeamDTO> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamDTO> teams) {
        this.teams = teams;
    }
}
