package com.betopia.hrm.domain.dto.leave;

import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LeaveBalanceEmployeeDTO {
    private Long id;
    private Long employeeId;
    private LeaveType leaveType;
    private Integer year;
    private BigDecimal entitledDays = BigDecimal.ZERO;
    private BigDecimal carriedForward = BigDecimal.ZERO;
    private BigDecimal encashed = BigDecimal.ZERO;
    private BigDecimal usedDays = BigDecimal.ZERO;
    private BigDecimal balance = BigDecimal.ZERO;
    private LocalDateTime deletedAt;

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public BigDecimal getEntitledDays() {
        return entitledDays;
    }

    public void setEntitledDays(BigDecimal entitledDays) {
        this.entitledDays = entitledDays;
    }

    public BigDecimal getCarriedForward() {
        return carriedForward;
    }

    public void setCarriedForward(BigDecimal carriedForward) {
        this.carriedForward = carriedForward;
    }

    public BigDecimal getEncashed() {
        return encashed;
    }

    public void setEncashed(BigDecimal encashed) {
        this.encashed = encashed;
    }

    public BigDecimal getUsedDays() {
        return usedDays;
    }

    public void setUsedDays(BigDecimal usedDays) {
        this.usedDays = usedDays;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
