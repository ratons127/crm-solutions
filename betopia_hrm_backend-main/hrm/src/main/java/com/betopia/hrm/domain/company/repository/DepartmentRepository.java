package com.betopia.hrm.domain.company.repository;


import com.betopia.hrm.domain.company.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    List<Department> findByWorkplaceIdOrderByIdDesc(Long workplaceId);

    Optional<Department> findFirstByNameIgnoreCase(String name);
}
