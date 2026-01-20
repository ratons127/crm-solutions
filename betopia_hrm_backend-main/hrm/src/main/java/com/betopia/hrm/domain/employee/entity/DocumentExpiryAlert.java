package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.DocumentExpiryAlertStatus;
import com.betopia.hrm.domain.users.entity.User;
import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Document_expiry_alerts")
public class DocumentExpiryAlert extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_document_id", nullable = false)
    private EmployeeDocument employeeDocument;

    @JoinColumn(name = "alert_date", nullable = false)
    private LocalDate alertDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sent_to")
    @JsonIgnore
    private User user;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20, nullable = false)
    private DocumentExpiryAlertStatus status = DocumentExpiryAlertStatus.SCHEDULED;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeDocument getEmployeeDocument() {
        return employeeDocument;
    }

    public void setEmployeeDocument(EmployeeDocument employeeDocument) {
        this.employeeDocument = employeeDocument;
    }

    public LocalDate getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(LocalDate alertDate) {
        this.alertDate = alertDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public DocumentExpiryAlertStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentExpiryAlertStatus status) {
        this.status = status;
    }
}
