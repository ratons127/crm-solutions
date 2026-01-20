package com.betopia.hrm.services.users.menu;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.users.entity.Menu;
import com.betopia.hrm.domain.users.entity.Permission;
import com.betopia.hrm.domain.users.request.MenuRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface MenuService {

    PaginationResponse<Menu> index(Sort.Direction direction, int page, int perPage);

    List<Menu> getAllMenus();

    Menu store(MenuRequest request);

    Menu edit(Long menuId);

    Menu update(Long menuId, MenuRequest request);

    void destroy(Long menuId);

    List<Menu> getAllMenusOrderedByParentId();
}
