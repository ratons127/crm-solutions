package com.betopia.hrm.domain.users.repository;

import com.betopia.hrm.domain.users.entity.Permission;
import com.betopia.hrm.domain.users.request.PermissionRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    boolean existsByName(String name);

    PermissionRequest findById(long id);
}
