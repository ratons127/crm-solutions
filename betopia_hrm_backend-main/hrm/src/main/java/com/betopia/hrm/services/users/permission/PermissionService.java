package com.betopia.hrm.services.users.permission;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.users.entity.Permission;
import com.betopia.hrm.domain.users.request.PermissionRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface PermissionService {

    PaginationResponse<Permission> index(Sort.Direction direction, int page, int perPage);

    List<Permission> getAllPermissions();

    Permission store(PermissionRequest request);

    Permission edit(Long permissionId);

    Permission update(Long permissionId, PermissionRequest request);

    void destroy(Long permissionId);
}
