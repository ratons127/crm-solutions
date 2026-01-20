package com.betopia.hrm.domain.employee.entity;


import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.AccessStatus;
import com.betopia.hrm.domain.employee.enums.SystemName;
import com.betopia.hrm.domain.users.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "access_revocation_log"
)
public class AccessRevocationLog extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separation_id", nullable = false)
    private EmployeeSeparations separation;

    @Enumerated(EnumType.STRING)
    @Column(name = "system_name")
    private SystemName systemName;

    @Column(name = "access_type", length = 100)
    private String accessType;

    @Column(name = "revocation_date", nullable = false)
    private LocalDateTime revocationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revoked_by")
    private User revokedBy;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_status")
    private AccessStatus accessStatus;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "retry_count")
    private int retryCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeSeparations getSeparation() {
        return separation;
    }

    public void setSeparation(EmployeeSeparations separation) {
        this.separation = separation;
    }

    public SystemName getSystemName() {
        return systemName;
    }

    public void setSystemName(SystemName systemName) {
        this.systemName = systemName;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public LocalDateTime getRevocationDate() {
        return revocationDate;
    }

    public void setRevocationDate(LocalDateTime revocationDate) {
        this.revocationDate = revocationDate;
    }

    public User getRevokedBy() {
        return revokedBy;
    }

    public void setRevokedBy(User revokedBy) {
        this.revokedBy = revokedBy;
    }

    public AccessStatus getAccessStatus() {
        return accessStatus;
    }

    public void setAccessStatus(AccessStatus accessStatus) {
        this.accessStatus = accessStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }
}
