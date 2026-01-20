package com.betopia.hrm.domain.company.entity;

import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "calendar_assigns")
public class CalendarAssign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "calendar_id", nullable = true)
    private Calendars calendar;

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

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 20, nullable = false)
    private Boolean status = true;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Calendars getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendars calendar) {
        this.calendar = calendar;
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
