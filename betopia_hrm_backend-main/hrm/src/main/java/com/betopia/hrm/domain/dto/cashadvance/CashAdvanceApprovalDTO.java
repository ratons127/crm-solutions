package com.betopia.hrm.domain.dto.cashadvance;

import java.time.LocalDateTime;

public class CashAdvanceApprovalDTO {

    private Long id;

    private Long advanceCashRequestId;

    private Long employeeId;

    private Long approverId;

    private Integer level;

    private String cashAdvanceStatus;

    private String remarks;

    private LocalDateTime actionDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAdvanceCashRequestId() {
        return advanceCashRequestId;
    }

    public void setAdvanceCashRequestId(Long advanceCashRequestId) {
        this.advanceCashRequestId = advanceCashRequestId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getCashAdvanceStatus() {
        return cashAdvanceStatus;
    }

    public void setCashAdvanceStatus(String cashAdvanceStatus) {
        this.cashAdvanceStatus = cashAdvanceStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getActionDate() {
        return actionDate;
    }

    public void setActionDate(LocalDateTime actionDate) {
        this.actionDate = actionDate;
    }
}
