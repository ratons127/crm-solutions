package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.InstituteName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstituteNameRepository extends JpaRepository<InstituteName, Long> {
}
