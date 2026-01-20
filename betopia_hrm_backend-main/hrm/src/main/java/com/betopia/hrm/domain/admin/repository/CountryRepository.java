package com.betopia.hrm.domain.admin.repository;

import com.betopia.hrm.domain.admin.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository  extends JpaRepository<Country, Long> {
}
