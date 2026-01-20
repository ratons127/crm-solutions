package com.betopia.hrm.domain.cashadvance.repository;

import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceSlabConfig;
import com.betopia.hrm.domain.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CashAdvanceSlabConfigRepository extends JpaRepository<CashAdvanceSlabConfig, Long>, JpaSpecificationExecutor<CashAdvanceSlabConfig> {
}
