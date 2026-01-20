package com.betopia.hrm.domain.company.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.company.enums.SeparationPolicyStatus;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "separation_policies")
public class SeparationPolicy extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @ManyToOne
    @JoinColumn(name = "workplace_id")
    private Workplace workplace;

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

    @OneToMany(mappedBy = "separationPolicy", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id DESC")
    private List<SeparationPolicyVersion> previousVersion = new ArrayList<>();

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "status")
    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Workplace getWorkplace() {
        return workplace;
    }

    public void setWorkplace(Workplace workplace) {
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

    public List<SeparationPolicyVersion> getPreviousVersion() {
        return previousVersion;
    }

    public void setPreviousVersion(List<SeparationPolicyVersion> previousVersion) {
        this.previousVersion = previousVersion;
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
