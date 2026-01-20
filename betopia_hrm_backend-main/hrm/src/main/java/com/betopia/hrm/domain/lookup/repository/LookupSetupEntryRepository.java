package com.betopia.hrm.domain.lookup.repository;

import com.betopia.hrm.domain.lookup.entity.LookupSetupEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LookupSetupEntryRepository extends JpaRepository<LookupSetupEntry, Long> {
}
