package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.EmployeeDocument;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface EmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Long> {
    List<EmployeeDocument> findAllByCurrentVersionIsNotNull(Sort sort);
}
