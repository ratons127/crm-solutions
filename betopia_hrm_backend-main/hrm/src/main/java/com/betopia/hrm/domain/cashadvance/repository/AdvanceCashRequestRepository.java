package com.betopia.hrm.domain.cashadvance.repository;

import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvanceCashRequestRepository extends JpaRepository<AdvanceCashRequest, Long> {

    @Query(value = "SELECT nextval('advance_cash_request_seq')", nativeQuery = true)
    long nextCashAdvanceRequestSerial();
}
