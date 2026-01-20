package com.betopia.hrm.domain.company.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.company.enums.AudienceType;
import com.betopia.hrm.domain.users.entity.Company;
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

import java.util.UUID;

@Entity
@Table(name = "notification_bindings")
public class NotificationBindings extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private NotificationEvents event;

    @Enumerated(EnumType.STRING)
    @Column(name = "audience_type")
    private AudienceType audienceType;

    @Column(name = "audience_ref_id")
    private UUID audienceRefId;

    @Column(name = "priority", nullable = false)
    private Integer priority = 100;

    @Column(name = "status", nullable = false)
    private Boolean status = Boolean.TRUE;


    // --- Getters & Setters ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public NotificationEvents getEvent() {
        return event;
    }

    public void setEvent(NotificationEvents event) {
        this.event = event;
    }

    public AudienceType getAudienceType() {
        return audienceType;
    }

    public void setAudienceType(AudienceType audienceType) {
        this.audienceType = audienceType;
    }

    public UUID getAudienceRefId() {
        return audienceRefId;
    }

    public void setAudienceRefId(UUID audienceRefId) {
        this.audienceRefId = audienceRefId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
