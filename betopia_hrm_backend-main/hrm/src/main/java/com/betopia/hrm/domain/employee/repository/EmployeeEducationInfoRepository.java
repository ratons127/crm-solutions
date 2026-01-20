package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.EmployeeEducationInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeEducationInfoRepository extends JpaRepository<EmployeeEducationInfo, Long> {
}
