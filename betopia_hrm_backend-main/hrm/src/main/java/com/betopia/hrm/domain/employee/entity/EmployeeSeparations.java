package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.ApprovalLevel;
import com.betopia.hrm.domain.employee.enums.SeparationStatus;
import com.betopia.hrm.domain.employee.enums.SeparationType;
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

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "employee_separations")
public class EmployeeSeparations extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Enumerated(EnumType.STRING)
    @Column(name = "separation_type", nullable = false)
    private SeparationType separationType;

    @Column(name = "submission_date", nullable = false)
    private LocalDate submissionDate;

    @Column(name = "requested_lwd", nullable = false)
    private LocalDate requestedLwd;

    @Column(name = "actual_lwd")
    private LocalDate actualLwd;

    @Column(name = "effective_separation_date")
    private LocalDate effectiveSeparationDate;

    @Column(length = 500)
    private String reason;

    @Column(name = "resignation_letter_path", length = 255)
    private String resignationLetterPath;

    @Enumerated(EnumType.STRING)
    @Column(name = "separations_status", nullable = false)
    private SeparationStatus separationsStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "current_approver_id")
    private User currentApprover;

    @Enumerated(EnumType.STRING)
    @Column(name = "current_approval_level")
    private ApprovalLevel currentApprovalLevel;

    @Column(name = "notice_period_days")
    private Integer noticePeriodDays = 30;

    @Column(name = "notice_waived")
    private Boolean noticeWaived = false;

    @Column(name = "notice_waiver_reason", columnDefinition = "TEXT")
    private String noticeWaiverReason;

    @Column(name = "notice_buyout_amount", precision = 10, scale = 2)
    private BigDecimal noticeBuyoutAmount = BigDecimal.valueOf(0.00);

    @Column(name = "is_buyout_recovered")
    private Boolean isBuyoutRecovered = false;

    // -------------------------
    // Getters and Setters
    // -------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employeeId) { this.employee = employeeId; }

    public SeparationType getSeparationType() { return separationType; }
    public void setSeparationType(SeparationType separationType) { this.separationType = separationType; }

    public LocalDate getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDate submissionDate) { this.submissionDate = submissionDate; }

    public LocalDate getRequestedLwd() { return requestedLwd; }
    public void setRequestedLwd(LocalDate requestedLwd) { this.requestedLwd = requestedLwd; }

    public LocalDate getActualLwd() { return actualLwd; }
    public void setActualLwd(LocalDate actualLwd) { this.actualLwd = actualLwd; }

    public LocalDate getEffectiveSeparationDate() { return effectiveSeparationDate; }
    public void setEffectiveSeparationDate(LocalDate effectiveSeparationDate) { this.effectiveSeparationDate = effectiveSeparationDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getResignationLetterPath() { return resignationLetterPath; }
    public void setResignationLetterPath(String resignationLetterPath) { this.resignationLetterPath = resignationLetterPath; }

    public SeparationStatus getSeparationsStatus() { return separationsStatus; }
    public void setSeparationsStatus(SeparationStatus separationsStatus) { this.separationsStatus = separationsStatus; }

    public User getCurrentApprover() { return currentApprover; }
    public void setCurrentApprover(User currentApproverId) { this.currentApprover = currentApprover; }

    public ApprovalLevel getCurrentApprovalLevel() { return currentApprovalLevel; }
    public void setCurrentApprovalLevel(ApprovalLevel currentApprovalLevel) { this.currentApprovalLevel = currentApprovalLevel; }

    public Integer getNoticePeriodDays() { return noticePeriodDays; }
    public void setNoticePeriodDays(Integer noticePeriodDays) { this.noticePeriodDays = noticePeriodDays; }

    public Boolean getNoticeWaived() { return noticeWaived; }
    public void setNoticeWaived(Boolean noticeWaived) { this.noticeWaived = noticeWaived; }

    public String getNoticeWaiverReason() { return noticeWaiverReason; }
    public void setNoticeWaiverReason(String noticeWaiverReason) { this.noticeWaiverReason = noticeWaiverReason; }

    public BigDecimal getNoticeBuyoutAmount() { return noticeBuyoutAmount; }
    public void setNoticeBuyoutAmount(BigDecimal noticeBuyoutAmount) { this.noticeBuyoutAmount = noticeBuyoutAmount; }

    public Boolean getIsBuyoutRecovered() { return isBuyoutRecovered; }
    public void setIsBuyoutRecovered(Boolean isBuyoutRecovered) { this.isBuyoutRecovered = isBuyoutRecovered; }

}
