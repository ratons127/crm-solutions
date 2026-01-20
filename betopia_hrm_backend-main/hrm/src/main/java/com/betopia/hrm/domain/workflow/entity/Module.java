package com.betopia.hrm.domain.workflow.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "modules")
public class Module extends Auditable<Long, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "module_name", nullable = false, unique = true)
    private String moduleName;

    @Column(name = "module_code", nullable = false, unique = true)
    private String moduleCode;

    private Boolean status = true;

    @OneToMany(mappedBy = "module", cascade = CascadeType.ALL)
    private List<WorkflowInstance> instances = new ArrayList<>();

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

    public List<WorkflowInstance> getInstances() {
        return instances;
    }

    public void setInstances(List<WorkflowInstance> instances) {
        this.instances = instances;
    }
}
