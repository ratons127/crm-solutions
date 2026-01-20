package com.betopia.hrm.domain.dto.company;

import com.fasterxml.jackson.annotation.JsonInclude;

public class TeamDTO {

    private Long id;
    private String name;
    private String code;
    private String description;
    private Long departmentId;

    public TeamDTO() {
    }

    public TeamDTO(Long id, String name, String code, String description, Long departmentId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.description = description;
        this.departmentId = departmentId;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
