package com.betopia.hrm.domain.users.repository;

import com.betopia.hrm.domain.users.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    boolean existsByName(String name);

    Optional<Company> findFirstByNameIgnoreCase(String name);
}
