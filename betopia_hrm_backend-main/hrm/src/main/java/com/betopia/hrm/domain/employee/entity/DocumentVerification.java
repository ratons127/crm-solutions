package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.DocumentVerificationStatus;
import com.betopia.hrm.domain.users.entity.User;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "document_verifications")
public class DocumentVerification extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_document_id", nullable = false)
    private EmployeeDocument employeeDocument;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "version_id", nullable = false)
    private EmployeeDocumentVersion employeeDocumentVersion;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "verified_by", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "verified_at")
    private LocalDateTime verifiedAt;

    @Column(name = "remarks")
    private String remarks;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private DocumentVerificationStatus status = DocumentVerificationStatus.PENDING;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeDocument getEmployeeDocument() {
        return employeeDocument;
    }

    public void setEmployeeDocument(EmployeeDocument employeeDocument) {
        this.employeeDocument = employeeDocument;
    }

    public EmployeeDocumentVersion getEmployeeDocumentVersion() {
        return employeeDocumentVersion;
    }

    public void setEmployeeDocumentVersion(EmployeeDocumentVersion employeeDocumentVersion) {
        this.employeeDocumentVersion = employeeDocumentVersion;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(LocalDateTime verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public DocumentVerificationStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentVerificationStatus status) {
        this.status = status;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
