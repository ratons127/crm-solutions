package com.betopia.hrm.domain.auth.login.repository;

import com.betopia.hrm.domain.auth.login.ResetPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResetPasswordRepository extends JpaRepository<ResetPassword, Long> {

    Optional<ResetPassword> findByToken(String token);

}
