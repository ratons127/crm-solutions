package com.betopia.hrm.domain.users.repository;

import com.betopia.hrm.domain.users.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {

    boolean existsByName(String name);

    @Query("SELECT m FROM Menu m ORDER BY m.parentId ASC")
    List<Menu> findAllByOrderByParentIdAsc();

    @Query("SELECT m FROM Menu m WHERE m.permission.id IN :permissionIds OR m.permission IS NULL ORDER BY m.parentId")
    List<Menu> findMenusByPermissionIds(@Param("permissionIds") List<Long> permissionIds);

    @Query("SELECT m FROM Menu m LEFT JOIN FETCH m.permission ORDER BY m.parentId ASC")
    List<Menu> findAllWithPermissions();

    @Query("SELECT COALESCE(MAX(m.menuOrder), 0) FROM Menu m " +
            "WHERE (m.parentId IS NULL AND :parentId IS NULL) OR (m.parentId = :parentId)")
    Optional<Long> findMaxMenuOrderByParentId(@Param("parentId") Long parentId);

}
