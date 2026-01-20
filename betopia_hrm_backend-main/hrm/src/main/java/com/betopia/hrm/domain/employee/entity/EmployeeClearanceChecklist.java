package com.betopia.hrm.domain.employee.entity;


import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.ClearanceStatus;
import com.betopia.hrm.domain.employee.enums.Departments;
import com.betopia.hrm.domain.users.entity.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "employee_clearance_checklist")
public class EmployeeClearanceChecklist extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separation_id")
    private EmployeeSeparations separation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clearance_item_id")
    private ExitClearanceItem clearanceItem;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Departments department;

    // Assigned to user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "clearance_status")
    private ClearanceStatus clearanceStatus;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "evidence_path", length = 255)
    private String evidencePath;

    @Column(name = "cleared_date")
    private LocalDateTime clearedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cleared_by")
    private User clearedBy;

    @Column(name = "sla_deadline", nullable = false)
    private LocalDateTime slaDeadline;

    @Column(name = "is_overdue")
    private Boolean isOverdue = false;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public EmployeeSeparations getSeparation() {
        return separation;
    }

    public void setSeparation(EmployeeSeparations separation) {
        this.separation = separation;
    }

    public ExitClearanceItem getClearanceItem() {
        return clearanceItem;
    }

    public void setClearanceItem(ExitClearanceItem clearanceItem) {
        this.clearanceItem = clearanceItem;
    }

    public Departments getDepartments() {
        return department;
    }

    public void setDepartments(Departments department) {
        this.department = department;
    }

    public User getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }

    public ClearanceStatus getClearanceStatus() {
        return clearanceStatus;
    }

    public void setClearanceStatus(ClearanceStatus clearanceStatus) {
        this.clearanceStatus = clearanceStatus;
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

    public LocalDateTime getClearedDate() {
        return clearedDate;
    }

    public void setClearedDate(LocalDateTime clearedDate) {
        this.clearedDate = clearedDate;
    }

    public User getClearedBy() {
        return clearedBy;
    }

    public void setClearedBy(User clearedBy) {
        this.clearedBy = clearedBy;
    }

    public LocalDateTime getSlaDeadline() {
        return slaDeadline;
    }

    public void setSlaDeadline(LocalDateTime slaDeadline) {
        this.slaDeadline = slaDeadline;
    }

    public Boolean getIsOverdue() {
        return isOverdue;
    }

    public void setIsOverdue(Boolean isOverdue) {
        this.isOverdue = isOverdue;
    }

}
