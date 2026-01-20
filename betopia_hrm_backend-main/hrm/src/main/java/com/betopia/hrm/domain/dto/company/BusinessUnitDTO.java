package com.betopia.hrm.domain.dto.company;

import com.fasterxml.jackson.annotation.JsonInclude;

public class BusinessUnitDTO {

    private Long id;
    private String name;
    private String code;
    private Long companyId;
    private String companyName;

    public BusinessUnitDTO() {
    }

    public BusinessUnitDTO(Long id, String name, String code, Long companyId, String companyName) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.companyId = companyId;
        this.companyName = companyName;
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

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
}
