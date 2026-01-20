package com.betopia.hrm.domain.cashadvance.repository;

import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashAdvanceApprovalRepository extends JpaRepository<CashAdvanceApproval, Long> {
}
