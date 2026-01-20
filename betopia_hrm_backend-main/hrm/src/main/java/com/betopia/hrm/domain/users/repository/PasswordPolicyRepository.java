package com.betopia.hrm.domain.users.repository;

import com.betopia.hrm.domain.users.entity.PasswordPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordPolicyRepository extends JpaRepository<PasswordPolicy,Long> {
}
