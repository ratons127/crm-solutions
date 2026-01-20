package com.betopia.hrm.domain.base.response;

import com.betopia.hrm.domain.users.entity.Menu;
import com.betopia.hrm.domain.users.entity.Permission;
import com.betopia.hrm.domain.users.entity.User;

import java.util.List;
import java.util.Map;

public class AuthResponse {

    private int statusCode;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private Long expiration;
    private User User;
    private Object role;
    private List<Permission> permissions;
    private List<Menu> menus;
    private Long employeeId;
    private Long companyId;

    private Integer employeeSerialId;

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getExpiration() {
        return expiration;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        User = user;
    }

    public Object getRole() {
        return role;
    }

    public void setRole(Object role) {
        this.role = role;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public Long getEmployeeId() {
        return employeeId;
    }
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getEmployeeSerialId() {
        return employeeSerialId;
    }

    public void setEmployeeSerialId(Integer employeeSerialId) {
        this.employeeSerialId = employeeSerialId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId){
        this.companyId = companyId;
    }
}
