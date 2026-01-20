package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.employee.enums.ApprovalLevel;
import com.betopia.hrm.domain.employee.enums.SeparationStatus;
import com.betopia.hrm.domain.employee.enums.SeparationType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EmployeeSeparationsDTO {

    private Long id;
    private Long employeeId;
    private SeparationType separationType;
    private LocalDate submissionDate;
    private LocalDate requestedLwd;
    private LocalDate actualLwd;
    private LocalDate effectiveSeparationDate;
    private String reason;
    private String resignationLetterPath;
    private SeparationStatus separationsStatus;
    private Long currentApproverId;
    private ApprovalLevel currentApprovalLevel;
    private Integer noticePeriodDays;
    private Boolean noticeWaived;
    private String noticeWaiverReason;
    private BigDecimal noticeBuyoutAmount;
    private Boolean isBuyoutRecovered;

    public EmployeeSeparationsDTO() {}

    public EmployeeSeparationsDTO(Long id, Long employeeId, SeparationType separationType,
                                  LocalDate submissionDate, LocalDate requestedLwd, LocalDate actualLwd,
                                  LocalDate effectiveSeparationDate, String reason, String resignationLetterPath,
                                  SeparationStatus separationsStatus, Long currentApproverId,
                                  ApprovalLevel currentApprovalLevel, Integer noticePeriodDays,
                                  Boolean noticeWaived, String noticeWaiverReason, BigDecimal noticeBuyoutAmount,
                                  Boolean isBuyoutRecovered) {
        this.id = id;
        this.employeeId = employeeId;
        this.separationType = separationType;
        this.submissionDate = submissionDate;
        this.requestedLwd = requestedLwd;
        this.actualLwd = actualLwd;
        this.effectiveSeparationDate = effectiveSeparationDate;
        this.reason = reason;
        this.resignationLetterPath = resignationLetterPath;
        this.separationsStatus = separationsStatus;
        this.currentApproverId = currentApproverId;
        this.currentApprovalLevel = currentApprovalLevel;
        this.noticePeriodDays = noticePeriodDays;
        this.noticeWaived = noticeWaived;
        this.noticeWaiverReason = noticeWaiverReason;
        this.noticeBuyoutAmount = noticeBuyoutAmount;
        this.isBuyoutRecovered = isBuyoutRecovered;
    }

    // -------------------------
    // Getters and Setters
    // -------------------------
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeID() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

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

    public Long getCurrentApproverId() { return currentApproverId; }
    public void setCurrentApproverId(Long currentApproverId) { this.currentApproverId = currentApproverId; }

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
