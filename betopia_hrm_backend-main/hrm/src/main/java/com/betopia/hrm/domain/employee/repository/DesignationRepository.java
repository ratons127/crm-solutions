package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DesignationRepository extends JpaRepository<Designation, Long> {

    Optional<Designation> findFirstByNameIgnoreCase(String name);
}
