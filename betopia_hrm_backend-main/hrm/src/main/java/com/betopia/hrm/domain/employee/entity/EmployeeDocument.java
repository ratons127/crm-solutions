package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.EmployeeDocumentStatus;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "employee_documents")
public class EmployeeDocument extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "document_type_id", nullable = false)
    private DocumentType documentType;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_status", length = 20, nullable = false)
    private EmployeeDocumentStatus documentStatus;

    @Column(name = "status", length = 20, nullable = false)
    private Boolean status;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_version_id", unique = true)
    private EmployeeDocumentVersion currentVersion;

    @OneToMany(mappedBy = "employeeDocument", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("doc-versions")
    @OrderBy("isCurrent DESC")
    private List<EmployeeDocumentVersion> employeeDocumentVersions = new ArrayList<>();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    public void addDetail(EmployeeDocumentVersion d) {
        employeeDocumentVersions.add(d);
        d.setEmployeeDocument(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
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

    public EmployeeDocumentStatus getDocumentStatus() {
        return documentStatus;
    }

    public void setDocumentStatus(EmployeeDocumentStatus documentStatus) {
        this.documentStatus = documentStatus;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public EmployeeDocumentVersion getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(EmployeeDocumentVersion currentVersion) {
        this.currentVersion = currentVersion;
    }

    public List<EmployeeDocumentVersion> getEmployeeDocumentVersions() {
        return employeeDocumentVersions;
    }
    public void setEmployeeDocumentVersions(List<EmployeeDocumentVersion> employeeDocumentVersions) {
        this.employeeDocumentVersions = employeeDocumentVersions;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
