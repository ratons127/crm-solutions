package com.betopia.hrm.domain.lookup.repository;

import com.betopia.hrm.domain.lookup.entity.LookupSetupDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LookupSetupDetailsRepository extends JpaRepository<LookupSetupDetails, Long> {

    Page<LookupSetupDetails> findBySetup_Id(Long setupId, Pageable pageable);

    Optional<LookupSetupDetails> findFirstByNameIgnoreCase(String name);
}
