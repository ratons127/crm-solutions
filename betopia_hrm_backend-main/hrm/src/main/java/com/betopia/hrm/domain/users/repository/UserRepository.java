package com.betopia.hrm.domain.users.repository;

import com.betopia.hrm.domain.leave.entity.LeaveGroupAssign;
import com.betopia.hrm.domain.users.entity.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    Optional<User> findByEmail(String email);

    Optional<User> findByPhone(String phone);

    Optional<User> findByEmailOrPhone(String email, String phone);

    Optional<User> findByEmployeeSerialId(Integer employeeSerialId);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM model_has_role WHERE user_id = ?1", nativeQuery = true)
    void deleteUserRoles(Long userId);
}
