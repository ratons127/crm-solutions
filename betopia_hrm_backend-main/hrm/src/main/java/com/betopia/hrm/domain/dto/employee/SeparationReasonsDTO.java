package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.employee.enums.SeparationType;

public class SeparationReasonsDTO {
    private Long id;
    private SeparationType separationType;
    private String reasonCode;
    private String reasonText;
    private Boolean status;
    private Integer displayOrder;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SeparationType getSeparationType() {
        return separationType;
    }

    public void setSeparationType(SeparationType separationType) {
        this.separationType = separationType;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonText() {
        return reasonText;
    }

    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
    }
}
