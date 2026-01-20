package com.betopia.hrm.domain.dto.company;

import com.fasterxml.jackson.annotation.JsonInclude;

public class WorkplaceGroupDTO {

    private Long id;
    private String name;
    private String code;
    private Long businessUnitId;

    public WorkplaceGroupDTO() {
    }

    public WorkplaceGroupDTO(Long id, String name, String code, Long businessUnitId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.businessUnitId = businessUnitId;
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

    public Long getBusinessUnitId() {
        return businessUnitId;
    }

    public void setBusinessUnitId(Long businessUnitId) {
        this.businessUnitId = businessUnitId;
    }
}
