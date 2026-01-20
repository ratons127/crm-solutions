package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.DocumentVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentVerificationRepository extends JpaRepository<DocumentVerification, Long> {
}
