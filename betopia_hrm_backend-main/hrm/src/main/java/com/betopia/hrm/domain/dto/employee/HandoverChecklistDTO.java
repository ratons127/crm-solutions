package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.employee.enums.HandoverStatus;

import java.time.LocalDateTime;

public class HandoverChecklistDTO {

    private Long id;
    private Long separationId;
    private String itemDescription;
    private Long handoverToId;
    private HandoverStatus status;
    private LocalDateTime completedDate;
    private String remarks;
    private String evidencePath;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSeparationId() {
        return separationId;
    }

    public void setSeparationId(Long separationId) {
        this.separationId = separationId;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Long getHandoverToId() {
        return handoverToId;
    }

    public void setHandoverToId(Long handoverToId) {
        this.handoverToId = handoverToId;
    }

    public HandoverStatus getStatus() {
        return status;
    }

    public void setStatus(HandoverStatus status) {
        this.status = status;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getEvidencePath() {
        return evidencePath;
    }

    public void setEvidencePath(String evidencePath) {
        this.evidencePath = evidencePath;
    }
}
