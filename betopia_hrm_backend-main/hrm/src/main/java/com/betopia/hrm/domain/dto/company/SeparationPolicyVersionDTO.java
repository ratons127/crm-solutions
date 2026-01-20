package com.betopia.hrm.domain.dto.company;

import com.betopia.hrm.domain.company.enums.SeparationPolicyStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

public class SeparationPolicyVersionDTO {

    private Long id;
    private Long companyId;
    private Long workplaceId;
    private String name;
    private String code;
    private String description;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private SeparationPolicyStatus separationPolicyStatus;
    private Long approvedBy;
    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public Long getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(Long workplaceId) {
        this.workplaceId = workplaceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDate effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDate getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDate effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public SeparationPolicyStatus getSeparationPolicyStatus() {
        return separationPolicyStatus;
    }

    public void setSeparationPolicyStatus(SeparationPolicyStatus separationPolicyStatus) {
        this.separationPolicyStatus = separationPolicyStatus;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
