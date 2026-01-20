package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.employee.enums.Departments;

public class ExitClearanceItemDTO {

    private Long id;
    private ExitClearanceTemplateDTO template;
    private Departments department;
    private String itemName;
    private String itemDescription;
    private Boolean isMandatory;
    private String assigneeRole;
    private Integer sequenceOrder;
    private Boolean status;

    // -------------------------
    // Getters and Setters
    // -------------------------
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ExitClearanceTemplateDTO getTemplate() {
        return template;
    }

    public void setTemplate(ExitClearanceTemplateDTO template) {
        this.template = template;
    }

    public Departments getDepartment() {
        return department;
    }

    public void setDepartment(Departments department) {
        this.department = department;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    public String getAssigneeRole() {
        return assigneeRole;
    }

    public void setAssigneeRole(String assigneeRole) {
        this.assigneeRole = assigneeRole;
    }

    public Integer getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(Integer sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
