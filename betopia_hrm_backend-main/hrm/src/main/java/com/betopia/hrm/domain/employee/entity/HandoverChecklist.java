package com.betopia.hrm.domain.employee.entity;


import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.HandoverStatus;
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
@Table(name = "handover_checklist")
public class HandoverChecklist extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation with EmployeeSeparations (many handover items per separation)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separation_id", nullable = false)
    private EmployeeSeparations separation;

    @Column(name = "item_description", nullable = false, length = 500)
    private String itemDescription;

    // The employee receiving the handover
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "handover_to")
    private Employee handoverTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private HandoverStatus status;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "remarks", columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "evidence_path", length = 255)
    private String evidencePath;

    // Getters & Setters
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

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Employee getHandoverTo() {
        return handoverTo;
    }

    public void setHandoverTo(Employee handoverTo) {
        this.handoverTo = handoverTo;
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
