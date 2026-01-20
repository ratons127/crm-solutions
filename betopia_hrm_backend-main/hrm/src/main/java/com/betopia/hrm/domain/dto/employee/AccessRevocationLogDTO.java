package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.dto.user.UserDTO;
import com.betopia.hrm.domain.employee.enums.AccessStatus;
import com.betopia.hrm.domain.employee.enums.SystemName;

import java.time.LocalDateTime;

public class AccessRevocationLogDTO {
    private Long id;
    private EmployeeSeparationsDTO separation;
    private SystemName systemName;
    private String accessType;
    private LocalDateTime revocationDate;
    private UserDTO revokedBy;
    private AccessStatus accessStatus;
    private String errorMessage;
    private int retryCount;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeSeparationsDTO getSeparation() {
        return separation;
    }

    public void setSeparation(EmployeeSeparationsDTO separation) {
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

    public UserDTO getRevokedBy() {
        return revokedBy;
    }

    public void setRevokedBy(UserDTO revokedBy) {
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
