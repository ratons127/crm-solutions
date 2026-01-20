package com.betopia.hrm.domain.dto.leave;

import com.betopia.hrm.domain.dto.company.BusinessUnitDTO;
import com.betopia.hrm.domain.dto.company.CompanyDTO;
import com.betopia.hrm.domain.dto.company.DepartmentDTO;
import com.betopia.hrm.domain.dto.company.TeamDTO;
import com.betopia.hrm.domain.dto.company.WorkplaceDTO;
import com.betopia.hrm.domain.dto.company.WorkplaceGroupDTO;

public class LeaveGroupAssignDTO {
    private Long id;
    private LeaveTypeDTO leaveType;
    private LeaveGroupDTO leaveGroup;
    private CompanyDTO company;
    private BusinessUnitDTO businessUnit;
    private WorkplaceGroupDTO workplaceGroup;
    private WorkplaceDTO workplace;
    private DepartmentDTO department;
    private TeamDTO team;
    private String description;
    private Boolean status = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveTypeDTO getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveTypeDTO leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveGroupDTO getLeaveGroup() {
        return leaveGroup;
    }

    public void setLeaveGroup(LeaveGroupDTO leaveGroup) {
        this.leaveGroup = leaveGroup;
    }

    public CompanyDTO getCompany() {
        return company;
    }

    public void setCompany(CompanyDTO company) {
        this.company = company;
    }

    public BusinessUnitDTO getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnitDTO businessUnit) {
        this.businessUnit = businessUnit;
    }

    public WorkplaceGroupDTO getWorkplaceGroup() {
        return workplaceGroup;
    }

    public void setWorkplaceGroup(WorkplaceGroupDTO workplaceGroup) {
        this.workplaceGroup = workplaceGroup;
    }

    public WorkplaceDTO getWorkplace() {
        return workplace;
    }

    public void setWorkplace(WorkplaceDTO workplace) {
        this.workplace = workplace;
    }

    public DepartmentDTO getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentDTO department) {
        this.department = department;
    }

    public TeamDTO getTeam() {
        return team;
    }

    public void setTeam(TeamDTO team) {
        this.team = team;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
