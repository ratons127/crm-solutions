package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.ExitSeparationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "exit_interview_templates")
public class ExitInterviewTemplates extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_name", nullable = false, length = 100)
    private String templateName;

    @Enumerated(EnumType.STRING)
    @Column(name = "separation_type", nullable = false)
    private ExitSeparationType separationType;

    @Column(name = "questions", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> questions;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "status")
    private Boolean status;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public ExitSeparationType getSeparationType() {
        return separationType;
    }

    public void setSeparationType(ExitSeparationType separationType) {
        this.separationType = separationType;
    }

    public Map<String, Object> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, Object> questions) {
        this.questions = questions;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
