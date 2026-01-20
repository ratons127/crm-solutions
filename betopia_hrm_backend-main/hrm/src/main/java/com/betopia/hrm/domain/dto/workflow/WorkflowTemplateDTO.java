package com.betopia.hrm.domain.dto.workflow;

import java.util.ArrayList;
import java.util.List;

public class WorkflowTemplateDTO {

    private Long id;
    private String templateName;
    private String description;
    private Boolean isDefault;
    private Boolean status;
    private Long moduleId;
    private List<WorkflowStageDTO> stagesDtos = new ArrayList<>();
    private List<WorkflowInstanceDTO> instancesDtos = new ArrayList<>();

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

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getModuleId() {
        return moduleId;
    }

    public void setModuleId(Long moduleId) {
        this.moduleId = moduleId;
    }

    public List<WorkflowStageDTO> getStagesDtos() {
        return stagesDtos;
    }

    public void setStagesDtos(List<WorkflowStageDTO> stagesDtos) {
        this.stagesDtos = stagesDtos;
    }

    public List<WorkflowInstanceDTO> getInstancesDtos() {
        return instancesDtos;
    }

    public void setInstancesDtos(List<WorkflowInstanceDTO> instancesDtos) {
        this.instancesDtos = instancesDtos;
    }
}
