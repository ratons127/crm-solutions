package com.betopia.hrm.domain.dto.employee;

public class ExitClearanceTemplateDTO {

    private Long id;
    private String templateName;
    private String description;
    private Boolean isDefault;
    private Boolean status;

    public ExitClearanceTemplateDTO() {}

    public ExitClearanceTemplateDTO(Long id, String templateName, String description, Boolean isDefault, Boolean status) {
        this.id = id;
        this.templateName = templateName;
        this.description = description;
        this.isDefault = isDefault;
        this.status = status;
    }


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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
