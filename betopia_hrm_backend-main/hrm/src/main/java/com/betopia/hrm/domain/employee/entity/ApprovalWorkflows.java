package com.betopia.hrm.domain.employee.entity;


import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.Modules;
import com.betopia.hrm.domain.users.entity.Role;
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

@Entity
@Table(name = "approval_workflows")
public class ApprovalWorkflows extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Modules module; // 'Transfer', 'Promotion', 'ActingAssignment'

    @Column(nullable = false)
    private Integer level; // Approval sequence: 1=Supervisor, 2=HR, 3=Admin

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_role_id", nullable = false)
    private Role approverRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id")
    private User approver;

    @Column(name = "sla_hours")
    private Integer slaHours;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "escalation_to")
    private Role escalationTo;


    @Column(length = 20)
    private Boolean status;

    // ------------------- Getters and Setters -------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Modules getModule() {
        return module;
    }

    public void setModule(Modules module) {
        this.module = module;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Role getApproverRole() {
        return approverRole;
    }

    public void setApproverRole(Role approverRole) {
        this.approverRole = approverRole;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }

    public Integer getSlaHours() {
        return slaHours;
    }

    public void setSlaHours(Integer slaHours) {
        this.slaHours = slaHours;
    }

    public Role getEscalationTo() {
        return escalationTo;
    }

    public void setEscalationTo(Role escalationTo) {
        this.escalationTo = escalationTo;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
