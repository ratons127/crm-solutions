package com.betopia.hrm.domain.dto.attendance;

import com.betopia.hrm.domain.dto.company.*;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;


@JsonInclude(JsonInclude.Include.ALWAYS)
public class ShiftRotationPatternDTO {

    private Long id;

    private String patternName;

    private CompanyDTO company;
    private BusinessUnitDTO businessUnit;
    private WorkplaceGroupDTO workPlaceGroup;
    private WorkplaceDTO workPlace;
    private DepartmentDTO department;
    private TeamDTO team;

    private String description;

    private Long rotationDays;

    private Boolean status;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<ShiftRotationPatternDetailDTO> patternDetails = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatternName() {
        return patternName;
    }

    public void setPatternName(String patternName) {
        this.patternName = patternName;
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

    public WorkplaceGroupDTO getWorkPlaceGroup() {
        return workPlaceGroup;
    }

    public void setWorkPlaceGroup(WorkplaceGroupDTO workPlaceGroup) {
        this.workPlaceGroup = workPlaceGroup;
    }

    public WorkplaceDTO getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(WorkplaceDTO workPlace) {
        this.workPlace = workPlace;
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

    public Long getRotationDays() {
        return rotationDays;
    }

    public void setRotationDays(Long rotationDays) {
        this.rotationDays = rotationDays;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<ShiftRotationPatternDetailDTO> getPatternDetails() {
        return patternDetails;
    }

    public void setPatternDetails(List<ShiftRotationPatternDetailDTO> patternDetails) {
        this.patternDetails = patternDetails;
    }
}
