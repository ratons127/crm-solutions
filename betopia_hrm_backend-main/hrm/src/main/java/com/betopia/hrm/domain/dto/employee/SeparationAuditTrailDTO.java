package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.dto.user.UserDTO;

import java.util.Map;

public class SeparationAuditTrailDTO {

    private Long id;
    private EmployeeSeparationsDTO separation;
    private String action;
    private UserDTO performedBy;
    private Map<String, Object> actionDetails;
    private Map<String, Object> oldValues;
    private Map<String, Object> newValues;
    private String ipAddress;
    private String userAgent;

    // Getters & Setters
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public UserDTO getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(UserDTO performedBy) {
        this.performedBy = performedBy;
    }

    public Map<String, Object> getActionDetails() {
        return actionDetails;
    }

    public void setActionDetails(Map<String, Object> actionDetails) {
        this.actionDetails = actionDetails;
    }

    public Map<String, Object> getOldValues() {
        return oldValues;
    }

    public void setOldValues(Map<String, Object> oldValues) {
        this.oldValues = oldValues;
    }

    public Map<String, Object> getNewValues() {
        return newValues;
    }

    public void setNewValues(Map<String, Object> newValues) {
        this.newValues = newValues;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

}
