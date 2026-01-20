package com.betopia.hrm.domain.employee.entity;


import com.betopia.hrm.domain.base.entity.Auditable;
import jakarta.persistence.*;

@Entity
@Table(name = "notice_period_config")
public class NoticePeriodConfig extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_separation_id", nullable = false)
    private EmployeeSeparations employeeSeparation;

    @Column(name = "default_notice_days", nullable = false)
    private Integer defaultNoticeDays = 30;

    @Column(name = "minimum_notice_days", nullable = false)
    private Integer minimumNoticeDays = 15;

    @Column(name = "grace_period_days")
    private Integer gracePeriodDays = 0;

    @Column(name = "can_waive")
    private Boolean canWaive = true;

    @Column(name = "can_buyout")
    private Boolean canBuyout = true;

    @Column(name = "status")
    private Boolean status = true;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeSeparations getEmployeeSeparation() {
        return employeeSeparation;
    }

    public void setEmployeeSeparation(EmployeeSeparations employeeSeparation) {
        this.employeeSeparation = employeeSeparation;
    }

    public Integer getDefaultNoticeDays() {
        return defaultNoticeDays;
    }

    public void setDefaultNoticeDays(Integer defaultNoticeDays) {
        this.defaultNoticeDays = defaultNoticeDays;
    }

    public Integer getMinimumNoticeDays() {
        return minimumNoticeDays;
    }

    public void setMinimumNoticeDays(Integer minimumNoticeDays) {
        this.minimumNoticeDays = minimumNoticeDays;
    }

    public Integer getGracePeriodDays() {
        return gracePeriodDays;
    }

    public void setGracePeriodDays(Integer gracePeriodDays) {
        this.gracePeriodDays = gracePeriodDays;
    }

    public Boolean getCanWaive() {
        return canWaive;
    }

    public void setCanWaive(Boolean canWaive) {
        this.canWaive = canWaive;
    }

    public Boolean getCanBuyout() {
        return canBuyout;
    }

    public void setCanBuyout(Boolean canBuyout) {
        this.canBuyout = canBuyout;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
