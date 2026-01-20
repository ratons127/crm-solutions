package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.employee.enums.EmployeeDocumentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EmployeeDocumentDTO {

    private Long id;
    private Long employeeId;
    private Long documentTypeId;
    private String filePath;
    private LocalDate issueDate;
    private LocalDate expiryDate;
    private Boolean status;
    private List<EmployeeDocumentVersionDTO> employeeDocumentVersions;
    private LocalDateTime deletedAt;
    private EmployeeDocumentStatus documentStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getDocumentTypeId() {
        return documentTypeId;
    }

    public void setDocumentTypeId(Long documentTypeId) {
        this.documentTypeId = documentTypeId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<EmployeeDocumentVersionDTO> getEmployeeDocumentVersions() {
        return employeeDocumentVersions;
    }

    public void setEmployeeDocumentVersions(List<EmployeeDocumentVersionDTO> employeeDocumentVersions) {
        this.employeeDocumentVersions = employeeDocumentVersions;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public EmployeeDocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(EmployeeDocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }
}
