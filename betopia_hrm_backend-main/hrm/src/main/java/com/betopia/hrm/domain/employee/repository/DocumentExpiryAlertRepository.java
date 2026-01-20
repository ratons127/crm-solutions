package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.DocumentExpiryAlert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentExpiryAlertRepository extends JpaRepository<DocumentExpiryAlert, Long> {
}
