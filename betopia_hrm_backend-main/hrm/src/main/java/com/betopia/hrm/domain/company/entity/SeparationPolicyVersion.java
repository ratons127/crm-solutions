package com.betopia.hrm.domain.company.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.company.enums.SeparationPolicyStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "separation_policy_versions")
public class SeparationPolicyVersion extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id")
    private long company;

    @Column(name = "workplace_id")
    private long workplace;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code", unique = true)
    private String code;

    @Column(name = "description", nullable = true)
    private String description;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "separation_policy_status")
    @Enumerated(EnumType.STRING)
    private SeparationPolicyStatus separationPolicyStatus;

    @Column(name = "previous_version_id")
    private Long previousVersionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separation_policy_id")
    private SeparationPolicy separationPolicy;

    @Column(name = "approved_by")
    private Long approvedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getCompany() {
        return company;
    }

    public void setCompany(long company) {
        this.company = company;
    }

    public long getWorkplace() {
        return workplace;
    }

    public void setWorkplace(long workplace) {
        this.workplace = workplace;
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

    public Long getPreviousVersionId() {
        return previousVersionId;
    }

    public void setPreviousVersionId(Long previousVersionId) {
        this.previousVersionId = previousVersionId;
    }

    public SeparationPolicy getSeparationPolicy() {
        return separationPolicy;
    }

    public void setSeparationPolicy(SeparationPolicy separationPolicy) {
        this.separationPolicy = separationPolicy;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }
}
