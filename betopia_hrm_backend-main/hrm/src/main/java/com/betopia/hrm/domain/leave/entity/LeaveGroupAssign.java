package com.betopia.hrm.domain.leave.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "leave_group_assigns")
public class LeaveGroupAssign extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "leave_type_id", nullable = true)
    private LeaveType leaveType;

    @ManyToOne
    @JoinColumn(name = "leave_group_id", nullable = true)
    private LeaveGroup leaveGroup;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = true)
    private Company company;

    @ManyToOne
    @JoinColumn(name = "business_unit_id", nullable = true)
    private BusinessUnit businessUnit;

    @ManyToOne
    @JoinColumn(name = "work_place_group_id", nullable = true)
    private WorkplaceGroup workplaceGroup;

    @ManyToOne
    @JoinColumn(name = "work_place_id", nullable = true)
    private Workplace workplace;

    @ManyToOne
    @JoinColumn(name = "department_id", nullable = true)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = true)
    private Team team;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(length = 10, nullable = false)
    private Boolean status = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveGroup getLeaveGroup() {
        return leaveGroup;
    }

    public void setLeaveGroup(LeaveGroup leaveGroup) {
        this.leaveGroup = leaveGroup;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public WorkplaceGroup getWorkplaceGroup() {
        return workplaceGroup;
    }

    public void setWorkplaceGroup(WorkplaceGroup workplaceGroup) {
        this.workplaceGroup = workplaceGroup;
    }

    public Workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(Workplace workplace) {
        this.workplace = workplace;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
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
