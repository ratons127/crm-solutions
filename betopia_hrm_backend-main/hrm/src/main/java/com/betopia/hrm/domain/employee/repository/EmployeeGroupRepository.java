package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.EmployeeGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeGroupRepository extends JpaRepository<EmployeeGroup, Long> {
}
