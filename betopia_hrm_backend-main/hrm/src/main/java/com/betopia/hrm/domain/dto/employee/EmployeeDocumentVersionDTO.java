package com.betopia.hrm.domain.dto.employee;

public class EmployeeDocumentVersionDTO {

    private Long id;

    private Long employeeDocumentId;

    private String filePath;

    private Long userId;

    private String remarks;

    private Boolean isCurrent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeDocumentId() {
        return employeeDocumentId;
    }

    public void setEmployeeDocumentId(Long employeeDocumentId) {
        this.employeeDocumentId = employeeDocumentId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getCurrent() {
        return isCurrent;
    }

    public void setCurrent(Boolean current) {
        isCurrent = current;
    }
}
