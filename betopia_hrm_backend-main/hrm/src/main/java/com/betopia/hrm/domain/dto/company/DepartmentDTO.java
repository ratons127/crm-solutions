package com.betopia.hrm.domain.dto.company;

import com.fasterxml.jackson.annotation.JsonInclude;

public class DepartmentDTO {

    private Long id;
    private String name;
    private String code;
    private String description;
    private Long workplaceId;

    public DepartmentDTO() {
    }

    public DepartmentDTO(Long id, String name, String code, String description, Long workplaceId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.workplaceId = workplaceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getWorkplaceId() {
        return workplaceId;
    }

    public void setWorkplaceId(Long workplaceId) {
        this.workplaceId = workplaceId;
    }
}
