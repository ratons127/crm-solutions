package com.betopia.hrm.domain.leave.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "leave_balance_employee")
public class LeaveBalanceEmployee extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "leave_type_id", nullable = false, updatable = false)
    private LeaveType leaveType;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "entitled_days", nullable = false)
    private BigDecimal entitledDays = BigDecimal.ZERO;

    @Column(name = "carried_forward", nullable = false)
    private BigDecimal carriedForward = BigDecimal.ZERO;

    @Column(name = "encashed", nullable = false)
    private BigDecimal encashed = BigDecimal.ZERO;

    @Column(name = "used_days", nullable = false)
    private BigDecimal usedDays = BigDecimal.ZERO;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "deleted_at")
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

    public boolean hasSufficientBalance(BigDecimal daysToConsume) {
        recalculateBalance();
        return safe(balance).compareTo(daysToConsume) >= 0;
    }

    /**
     * Recalculate & update the balance from constituent fields.
     * Balance = entitledDays + carriedForward - usedDays - encashed
     */
    public void recalculateBalance() {
        this.balance = safe(entitledDays)
                .add(safe(carriedForward))
                .subtract(safe(usedDays))
                .subtract(safe(encashed));
    }

    /**
     * Consume leave units from balance and usedDays.
     * Returns true if fully consumed; false if insufficient.
     */
    public boolean consumeLeave(BigDecimal daysToConsume) {
        recalculateBalance();
        BigDecimal available = safe(balance);

        if (available.compareTo(daysToConsume) >= 0) {
            this.usedDays = safe(usedDays).add(daysToConsume);
            recalculateBalance();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Apply carry forward at year-end.
     *
     * @param cap                carry forward cap (null => unlimited)
     * @param encashExcessAllowed if true, excess can be encashed by external payroll
     * @return excess days beyond carry-forward cap
     */
    public BigDecimal applyCarryForward(BigDecimal cap, boolean encashExcessAllowed) {
        recalculateBalance();
        BigDecimal remaining = safe(balance);

        BigDecimal carryCap = (cap == null) ? remaining : cap;
        BigDecimal carry = remaining.min(carryCap);

        this.carriedForward = carry;

        BigDecimal excess = remaining.subtract(carry).max(BigDecimal.ZERO);
        recalculateBalance();
        return excess;
    }

    /**
     * Encash N days (reduce balance, increase encashed).
     * Returns true if any days were encashed.
     */
    public boolean encash(BigDecimal days) {
        recalculateBalance();
        BigDecimal available = safe(balance);
        BigDecimal toEncash = days.min(available.max(BigDecimal.ZERO));

        if (toEncash.compareTo(BigDecimal.ZERO) <= 0) return false;

        this.encashed = safe(encashed).add(toEncash);
        recalculateBalance();
        return true;
    }

    // -----------------------
    // Utility
    // -----------------------

    private static BigDecimal safe(BigDecimal v) {
        return v == null ? BigDecimal.ZERO : v;
    }

}
