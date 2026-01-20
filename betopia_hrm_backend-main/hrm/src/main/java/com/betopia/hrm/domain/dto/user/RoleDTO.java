package com.betopia.hrm.domain.dto.user;

import java.util.List;

public class RoleDTO {

    private Long id;
    private String name;
    private String guardName;
    private List<PermissionDTO> permissions;

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

    public List<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDTO> permissions) {
        this.permissions = permissions;
    }
}
