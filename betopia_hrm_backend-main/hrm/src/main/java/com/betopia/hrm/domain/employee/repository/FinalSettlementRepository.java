package com.betopia.hrm.domain.employee.repository;


import com.betopia.hrm.domain.employee.entity.FinalSettlement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinalSettlementRepository extends JpaRepository<FinalSettlement, Long> {
}
