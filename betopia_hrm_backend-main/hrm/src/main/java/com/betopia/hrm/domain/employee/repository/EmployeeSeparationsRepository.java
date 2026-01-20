package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.EmployeeSeparations;
import com.betopia.hrm.domain.employee.enums.SeparationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeSeparationsRepository extends JpaRepository<EmployeeSeparations, Long> {

    List<EmployeeSeparations> findBySeparationsStatus(SeparationStatus separationsStatus);

}
