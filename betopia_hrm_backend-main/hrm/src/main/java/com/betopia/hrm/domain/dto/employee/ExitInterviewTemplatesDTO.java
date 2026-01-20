package com.betopia.hrm.domain.dto.employee;

import java.util.Map;

public class ExitInterviewTemplatesDTO {

    private Long id;
    private String templateName;
    private String separationType; // enum as String
    private Map<String, Object> questions; // JSON as String
    private Boolean isDefault;
    private Boolean status;

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

    public String getSeparationType() {
        return separationType;
    }

    public void setSeparationType(String separationType) {
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
