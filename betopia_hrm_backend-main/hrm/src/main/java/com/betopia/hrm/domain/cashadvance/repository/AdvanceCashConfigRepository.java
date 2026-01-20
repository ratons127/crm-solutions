package com.betopia.hrm.domain.cashadvance.repository;

import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdvanceCashConfigRepository extends JpaRepository<AdvanceCashConfig, Long> {

}
