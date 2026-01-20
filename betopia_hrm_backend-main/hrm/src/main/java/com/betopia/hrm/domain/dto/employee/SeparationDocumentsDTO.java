package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.dto.user.UserDTO;
import com.betopia.hrm.domain.employee.enums.SeparationDocumentType;

import java.time.LocalDateTime;

public class SeparationDocumentsDTO {

    private Long id;
    private EmployeeSeparationsDTO separation;
    private SeparationDocumentType documentType;
    private String documentName;
    private String filePath;
    private String mimeType;
    private Boolean generatedBySystem;
    private UserDTO uploadedBy;
    private Boolean isSigned;
    private LocalDateTime signedDate;
    private Boolean isEmployeeAccessible;

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

    public void setSeparation(EmployeeSeparationsDTO separationId) {
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

    public UserDTO getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(UserDTO uploadedBy) {
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
