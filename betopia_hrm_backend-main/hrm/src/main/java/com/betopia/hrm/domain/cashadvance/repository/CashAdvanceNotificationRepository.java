package com.betopia.hrm.domain.cashadvance.repository;

import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceNotifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CashAdvanceNotificationRepository extends JpaRepository<CashAdvanceNotifications, Long> {
}
