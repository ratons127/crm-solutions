package com.betopia.hrm.services.users.permission.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.users.entity.Permission;
import com.betopia.hrm.domain.users.repository.PermissionRepository;
import com.betopia.hrm.domain.users.request.PermissionRequest;
import com.betopia.hrm.services.users.permission.PermissionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionServiceImpl implements PermissionService {

    public final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PaginationResponse<Permission> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<Permission> permissionPage = permissionRepository.findAll(pageable);

        List<Permission> permissions = permissionPage.getContent();

        PaginationResponse<Permission> response = new PaginationResponse<>();

        response.setData(permissions);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All permissions fetch successful");

        Links links = Links.fromPage(permissionPage, "/permissions");
        response.setLinks(links);

        Meta meta = Meta.fromPage(permissionPage, "/permissions");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<Permission> getAllPermissions() {

        return permissionRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Permission store(PermissionRequest request) {

        Permission permission = new Permission();

        permission.setName(request.name());
        permission.setGuardName("api");

        return permissionRepository.save(permission);
    }

    @Override
    public Permission edit(Long permissionId) {

        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));

        return permission;
    }

    @Override
    public Permission update(Long permissionId, PermissionRequest request) {

        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));

        permission.setName(request.name() != null ? request.name() : permission.getName());
        permission.setGuardName("api");

        permissionRepository.save(permission);

        return permission;
    }

    @Override
    public void destroy(Long permissionId) {

        Permission permission = permissionRepository.findById(permissionId).orElseThrow(() -> new RuntimeException("Permission not found"));

        permissionRepository.delete(permission);
    }
}
