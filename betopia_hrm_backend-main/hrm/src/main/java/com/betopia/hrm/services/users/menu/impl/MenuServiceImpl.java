package com.betopia.hrm.services.users.menu.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.users.entity.Menu;
import com.betopia.hrm.domain.users.entity.Permission;
import com.betopia.hrm.domain.users.exception.menu.MenuNotFoundException;
import com.betopia.hrm.domain.users.repository.MenuRepository;
import com.betopia.hrm.domain.users.repository.PermissionRepository;
import com.betopia.hrm.domain.users.request.MenuRequest;
import com.betopia.hrm.services.users.menu.MenuService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    private final PermissionRepository permissionRepository;

    public MenuServiceImpl(MenuRepository menuRepository, PermissionRepository permissionRepository) {
        this.menuRepository = menuRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PaginationResponse<Menu> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "menuOrder"));

        Page<Menu> menuPage = menuRepository.findAll(pageable);

        List<Menu> menus = menuPage.getContent();

        PaginationResponse<Menu> response = new PaginationResponse<>();

        response.setData(menus);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All menus fetch successful");

        Links links = Links.fromPage(menuPage, "/menus");
        response.setLinks(links);

        Meta meta = Meta.fromPage(menuPage, "/menus");
        response.setMeta(meta);

        return response;
    }

    public List<Menu> getAllMenusOrderedByParentId() {
        return menuRepository.findAllWithPermissions();
    }

    @Override
    public List<Menu> getAllMenus() {

        return menuRepository.findAll(Sort.by(Sort.Direction.ASC, "menuOrder"));
    }

    @Override
    public Menu store(MenuRequest request) {

        Menu menu = new Menu();

        if (request.permission_id() != null)
        {
            Permission permission = permissionRepository.findById(request.permission_id())
                    .orElseThrow(() -> new RuntimeException("Permission not found with id: " + request.permission_id()));

            menu.setPermission(permission);
        }

        menu.setParentId(request.parentId());
        menu.setName(request.name());
        menu.setUrl(request.url());
        menu.setIcon(request.icon());
        menu.setHeaderMenu(request.headerMenu());
        menu.setSidebarMenu(request.sidebarMenu());
        menu.setDropdownMenu(request.dropdownMenu());
        menu.setChildrenParentMenu(request.childrenParentMenu());
        menu.setStatus(request.status());
        menu.setMenuOrder(request.menuOrder());

//        // --- Auto-generate menu order ---
//        Long parentId = request.parentId();
//        Long nextOrder = menuRepository.findMaxMenuOrderByParentId(parentId)
//                .map(max -> max + 1)
//                .orElse(1L); // start with 1 if none exist
//
//        menu.setMenuOrder(nextOrder);

        menuRepository.save(menu);

        return menu;
    }

    @Override
    public Menu edit(Long menuId) {

        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("Menu not found with id: " + menuId));

        return menu;
    }

    @Override
    public Menu update(Long menuId, MenuRequest request) {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new MenuNotFoundException("Menu id not found" + menuId));

        if (request.permission_id() != null)
        {
            Permission permission = permissionRepository.findById(request.permission_id())
                    .orElseThrow(() -> new RuntimeException("Permission id not found"));

            menu.setPermission(permission);
        }

        menu.setParentId(request.parentId());


        menu.setName(request.name() != null ? request.name() : menu.getName());
        menu.setUrl(request.url() != null ? request.url() : menu.getUrl());
        menu.setIcon(request.icon() != null ? request.icon() : menu.getIcon());
        menu.setHeaderMenu(request.headerMenu() != menu.isHeaderMenu() ? request.headerMenu() : menu.isHeaderMenu());
        menu.setSidebarMenu(request.sidebarMenu() != menu.isSidebarMenu() ? request.sidebarMenu() : menu.isSidebarMenu());
        menu.setDropdownMenu(request.dropdownMenu() != menu.isDropdownMenu() ? request.dropdownMenu() : menu.isDropdownMenu());
        menu.setChildrenParentMenu(request.childrenParentMenu() ? request.childrenParentMenu() : menu.isChildrenParentMenu());
        menu.setStatus(request.status() != menu.isStatus() ? request.status() : menu.isStatus());
        menu.setMenuOrder(request.menuOrder() != null ? request.menuOrder() : menu.getMenuOrder());

        return menuRepository.save(menu);
    }

    @Override
    public void destroy(Long menuId) {

        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new MenuNotFoundException("Menu id not found" + menuId));
        menuRepository.delete(menu);
    }
}
