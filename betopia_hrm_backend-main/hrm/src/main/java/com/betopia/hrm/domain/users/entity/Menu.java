package com.betopia.hrm.domain.users.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "menus")
public class Menu extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "permission_id", nullable = true)
    private Permission permission;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "name", nullable = true)
    private String name;

    @Column(name = "url", nullable = true)
    private String url;

    @Column(name = "icon", nullable = true)
    private String icon;

    @Column(name = "header_menu", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean headerMenu = false;

    @Column(name = "sidebar_menu", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean sidebarMenu = false;

    @Column(name = "dropdown_menu", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean dropdownMenu = false;

    @Column(name = "children_parent_menu")
    private boolean childrenParentMenu;

    @Column(name = "status", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean status = false;

    @Column(name = "menu_order", nullable = true)
    private Long menuOrder;

    @Transient
    private List<Menu> children = new ArrayList<>();

    public Long getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Long menuOrder) {
        this.menuOrder = menuOrder;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isHeaderMenu() {
        return headerMenu;
    }

    public void setHeaderMenu(boolean headerMenu) {
        this.headerMenu = headerMenu;
    }

    public boolean isSidebarMenu() {
        return sidebarMenu;
    }

    public void setSidebarMenu(boolean sidebarMenu) {
        this.sidebarMenu = sidebarMenu;
    }

    public boolean isDropdownMenu() {
        return dropdownMenu;
    }

    public void setDropdownMenu(boolean dropdownMenu) {
        this.dropdownMenu = dropdownMenu;
    }

    public boolean isChildrenParentMenu() {
        return childrenParentMenu;
    }

    public void setChildrenParentMenu(boolean childrenParentMenu) {
        this.childrenParentMenu = childrenParentMenu;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<Menu> getChildren() {
        return children;
    }

    public void setChildren(List<Menu> children) {
        this.children = children;
    }
}
