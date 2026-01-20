package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.EmployeeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, Long> {

    Optional<EmployeeType> findFirstByNameIgnoreCase(String name);
}
