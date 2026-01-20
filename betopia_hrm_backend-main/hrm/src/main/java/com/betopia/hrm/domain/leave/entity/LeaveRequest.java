package com.betopia.hrm.domain.leave.entity;

import com.betopia.hrm.domain.leave.enums.LeaveStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "leave_requests")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_group_assign_id", nullable = false)
    private LeaveGroupAssign leaveGroupAssign;

    @Column(name = "half_day")
    private Boolean halfDay = false; // true = half day, false = full day

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "days_requested")
    private BigDecimal daysRequested;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @OneToMany(mappedBy = "leaveRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LeaveRequestDocument> proofDocumentPath = new ArrayList<>();

    @Column(name = "justification")
    private String justification; // optional, only used when overlaps or exceptions

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LeaveStatus status;

    @Column(name = "requested_at")
    private LocalDateTime requestedAt;

    @JoinColumn(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "covering_employee_id")
    private Long coveringEmployeeId;

    @Column(name = "created_at")
    private LocalDateTime createdDate;

    @Column(name = "updated_at")
    private LocalDateTime lastModifiedDate;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "last_modified_by")
    private Long lastModifiedBy;

    @Version
    @JsonIgnore
    private Long version;

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

    public LeaveType getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }

    public LeaveGroupAssign getLeaveGroupAssign() {
        return leaveGroupAssign;
    }

    public void setLeaveGroupAssign(LeaveGroupAssign leaveGroupAssign) {
        this.leaveGroupAssign = leaveGroupAssign;
    }

    public Boolean getHalfDay() {
        return halfDay;
    }

    public void setHalfDay(Boolean halfDay) {
        this.halfDay = halfDay;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getDaysRequested() {
        return daysRequested;
    }

    public void setDaysRequested(BigDecimal daysRequested) {
        this.daysRequested = daysRequested;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<LeaveRequestDocument> getProofDocumentPath() {
        return proofDocumentPath;
    }

    public void setProofDocumentPath(List<LeaveRequestDocument> proofDocumentPath) {
        this.proofDocumentPath = proofDocumentPath;
    }

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }

    public LeaveStatus getStatus() {
        return status;
    }

    public void setStatus(LeaveStatus status) {
        this.status = status;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public Long getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime approvedAt) {
        this.approvedAt = approvedAt;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getCoveringEmployeeId() {
        return coveringEmployeeId;
    }

    public void setCoveringEmployeeId(Long coveringEmployeeId) {
        this.coveringEmployeeId = coveringEmployeeId;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
