package com.betopia.hrm.domain.employee.repository;


import com.betopia.hrm.domain.employee.entity.EmployeeClearanceChecklist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeClearanceChecklistRepository extends JpaRepository<EmployeeClearanceChecklist, Long> {
}
