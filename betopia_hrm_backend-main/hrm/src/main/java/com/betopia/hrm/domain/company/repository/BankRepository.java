package com.betopia.hrm.domain.company.repository;

import com.betopia.hrm.domain.company.entity.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
}
