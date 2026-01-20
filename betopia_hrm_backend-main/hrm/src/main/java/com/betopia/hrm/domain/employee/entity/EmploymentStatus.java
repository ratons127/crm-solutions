package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.base.annotation.ExposeField;
import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.users.entity.Company;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "employment_status")
public class EmploymentStatus extends Auditable<Long, Long> {


    @ExposeField
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExposeField
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ExposeField
    @Column(name = "status_code", nullable = false, unique = true, length = 50)
    private String statusCode;

    @ExposeField
    @Column(name = "status_name", nullable = false, length = 100)
    private String statusName;

    @ExposeField
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_system", nullable = false)
    private Boolean isSystem = false;

    @Column(name = "status", nullable = false)
    private Boolean status = true;

    // --- Getters & Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsSystem() {
        return isSystem;
    }

    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
