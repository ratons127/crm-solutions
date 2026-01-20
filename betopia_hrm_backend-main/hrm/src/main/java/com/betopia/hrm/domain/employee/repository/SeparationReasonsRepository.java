package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.SeparationReasons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeparationReasonsRepository extends JpaRepository<SeparationReasons, Long> {
}
