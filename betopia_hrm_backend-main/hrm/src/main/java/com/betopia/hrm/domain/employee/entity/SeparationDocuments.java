package com.betopia.hrm.domain.employee.entity;


import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.SeparationDocumentType;
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
@Table(name = "separation_documents")
public class SeparationDocuments extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separation_id")
    private EmployeeSeparations separation;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private SeparationDocumentType documentType;

    @Column(name = "document_name")
    private String documentName;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "generated_by_system")
    private Boolean generatedBySystem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_by")
    private User uploadedBy;

    @Column(name = "is_signed")
    private Boolean isSigned;

    @Column(name = "signed_date")
    private LocalDateTime signedDate;

    @Column(name = "is_employee_accessible")
    private Boolean isEmployeeAccessible;

    // Getters and Setters
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

    public SeparationDocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(SeparationDocumentType documentType) {
        this.documentType = documentType;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Boolean getGeneratedBySystem() {
        return generatedBySystem;
    }

    public void setGeneratedBySystem(Boolean generatedBySystem) {
        this.generatedBySystem = generatedBySystem;
    }

    public User getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(User uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public Boolean getIsSigned() {
        return isSigned;
    }

    public void setIsSigned(Boolean isSigned) {
        this.isSigned = isSigned;
    }

    public LocalDateTime getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(LocalDateTime signedDate) {
        this.signedDate = signedDate;
    }

    public Boolean getIsEmployeeAccessible() {
        return isEmployeeAccessible;
    }

    public void setIsEmployeeAccessible(Boolean isEmployeeAccessible) {
        this.isEmployeeAccessible = isEmployeeAccessible;
    }
}
