package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentTypeRepository extends JpaRepository<DocumentType, Long> {
}
