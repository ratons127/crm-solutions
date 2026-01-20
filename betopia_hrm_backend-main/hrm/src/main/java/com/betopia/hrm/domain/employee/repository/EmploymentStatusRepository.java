package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.EmploymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmploymentStatusRepository extends JpaRepository<EmploymentStatus, Long> {
}
