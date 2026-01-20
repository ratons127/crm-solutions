package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.EmployeeWorkExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeWorkExperienceRepository extends JpaRepository<EmployeeWorkExperience,Long> {
}
