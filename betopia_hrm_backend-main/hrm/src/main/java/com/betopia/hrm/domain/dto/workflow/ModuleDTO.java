package com.betopia.hrm.domain.dto.workflow;

import java.util.ArrayList;
import java.util.List;

public class ModuleDTO {
    private Long id;
    private String moduleName;
    private String moduleCode;
    private Boolean status;
    private List<WorkflowInstanceDTO> instancesDtos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<WorkflowInstanceDTO> getInstancesDtos() {
        return instancesDtos;
    }

    public void setInstancesDtos(List<WorkflowInstanceDTO> instancesDtos) {
        this.instancesDtos = instancesDtos;
    }
}
