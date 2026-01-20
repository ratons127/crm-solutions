package com.betopia.hrm.domain.company.repository;

import com.betopia.hrm.domain.company.entity.NotificationBindings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationBindingsRepository extends JpaRepository<NotificationBindings,Long> {
}
