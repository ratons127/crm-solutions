package com.betopia.hrm.domain.company.repository;

import com.betopia.hrm.domain.company.entity.BusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Long> {
    List<BusinessUnit> findByCompanyIdOrderByIdDesc(Long companyId);

    Optional<BusinessUnit> findFirstByNameIgnoreCase(String name);
}
