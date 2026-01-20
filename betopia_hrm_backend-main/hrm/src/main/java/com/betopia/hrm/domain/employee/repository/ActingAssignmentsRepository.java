package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.ActingAssignments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActingAssignmentsRepository extends JpaRepository<ActingAssignments, Long> {
}
