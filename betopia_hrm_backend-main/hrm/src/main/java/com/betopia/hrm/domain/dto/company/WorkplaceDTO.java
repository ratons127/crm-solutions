package com.betopia.hrm.domain.dto.company;

import com.fasterxml.jackson.annotation.JsonInclude;

public class WorkplaceDTO {

    private Long id;
    private String name;
    private String code;
    private String address;
    private Long workplaceGroupId;

    public WorkplaceDTO() {
    }

    public WorkplaceDTO(Long id, String name, String code, String address, Long workplaceGroupId) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.address = address;
        this.workplaceGroupId = workplaceGroupId;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getWorkplaceGroupId() {
        return workplaceGroupId;
    }

    public void setWorkplaceGroupId(Long workplaceGroupId) {
        this.workplaceGroupId = workplaceGroupId;
    }
}
