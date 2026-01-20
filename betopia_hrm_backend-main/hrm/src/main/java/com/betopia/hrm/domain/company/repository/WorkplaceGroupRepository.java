package com.betopia.hrm.domain.company.repository;

import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkplaceGroupRepository extends JpaRepository<WorkplaceGroup, Long> {
    List<WorkplaceGroup> findByBusinessUnitIdOrderByIdDesc(Long businessUnitId);

    Optional<WorkplaceGroup> findFirstByNameIgnoreCase(String name);
}
