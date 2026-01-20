package com.betopia.hrm.domain.dto.user;

public class PermissionDTO {

    private Long id;
    private String name;
    private String guardName;

    // -------------------
    // Getters and Setters
    // -------------------

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

    public String getGuardName() {
        return guardName;
    }

    public void setGuardName(String guardName) {
        this.guardName = guardName;
    }
}
