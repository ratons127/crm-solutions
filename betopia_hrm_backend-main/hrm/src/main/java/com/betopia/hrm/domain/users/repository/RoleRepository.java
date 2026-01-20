package com.betopia.hrm.domain.users.repository;

import com.betopia.hrm.domain.users.entity.Role;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByName(String name);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM role_has_permission WHERE role_id = ?1", nativeQuery = true)
    void deleteRolePermissions(Long roleId);
}
