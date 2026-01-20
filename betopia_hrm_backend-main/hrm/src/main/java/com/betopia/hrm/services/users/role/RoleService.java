package com.betopia.hrm.services.users.role;

import com.betopia.hrm.domain.users.entity.Role;
import com.betopia.hrm.domain.users.request.RoleRequest;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

public interface RoleService {

    PaginationResponse<Role> index(Sort.Direction direction, int page, int perPage);

    List<Role> getAllRoles();

    Role store(RoleRequest request);

    Map<String, Object> show(Long roleId);

    Role update(Long roleId, RoleRequest request);

    void destroy(Long roleId);
}
