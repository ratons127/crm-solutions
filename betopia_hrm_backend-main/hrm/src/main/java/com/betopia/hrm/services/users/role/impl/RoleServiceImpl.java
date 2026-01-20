package com.betopia.hrm.services.users.role.impl;

import com.betopia.hrm.domain.users.entity.Permission;
import com.betopia.hrm.domain.users.entity.Role;
import com.betopia.hrm.domain.users.exception.role.RoleAlreadyExistException;
import com.betopia.hrm.domain.users.exception.role.RoleNotFoundException;
import com.betopia.hrm.domain.users.repository.PermissionRepository;
import com.betopia.hrm.domain.users.repository.RoleRepository;
import com.betopia.hrm.domain.users.request.RoleRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.users.role.RoleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public PaginationResponse<Role> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<Role> rolePage = roleRepository.findAll(pageable);
        List<Role> roles = rolePage.getContent();

        PaginationResponse<Role> response = new PaginationResponse<>();

        response.setData(roles);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All roles fetch successful");

        Links links = Links.fromPage(rolePage, "/role");
        response.setLinks(links);

        Meta meta = Meta.fromPage(rolePage, "/role");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<Role> getAllRoles() {

        return roleRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Role store(RoleRequest request) {

        List<Permission> permissions = permissionRepository.findAllById(request.permissions());

        Role role = new Role();
        role.setName(request.name());
        role.setLevel(request.level() == null ? "level-4" : request.level());
        role.setGuardName("api");
        role.setPermissions(permissions);

        return roleRepository.save(role);
    }

    @Override
    public Map<String, Object> show(Long roleId) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));

        Map<String, Object> transformedRole = new HashMap<>();
        transformedRole.put("id", role.getId());
        transformedRole.put("role", role.getName());

        // Directly map permissions into a list of objects
        List<Map<String, Object>> permissions = role.getPermissions().stream()
                .map(permission -> {
                    Map<String, Object> permissionData = new HashMap<>();
                    permissionData.put("id", permission.getId());
                    permissionData.put("name", permission.getName());
                    return permissionData;
                })
                .collect(Collectors.toList());

        transformedRole.put("permissions", permissions);

        return transformedRole;
    }

    @Override
    public Role update(Long roleId, RoleRequest request) {
        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException("Role not found with id: " + roleId));

        role.setName(request.name() != null ? request.name() : role.getName());
        role.setLevel(request.level() != null ? request.level() : role.getLevel());
        role.setGuardName("api");

        role.getPermissions().clear();

        List<Permission> newPermissions = permissionRepository.findAllById(request.permissions());

        role.getPermissions().addAll(newPermissions);

        return roleRepository.save(role);
    }

    @Override
    public void destroy(Long roleId) {

        Role role = roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException("Role id is not found : " + roleId));

        if (role.getId() != null)
        {
            roleRepository.deleteRolePermissions(roleId);
        }

        roleRepository.delete(role);
    }
}
