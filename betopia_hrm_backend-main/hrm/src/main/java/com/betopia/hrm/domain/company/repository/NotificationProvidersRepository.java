package com.betopia.hrm.domain.company.repository;

import com.betopia.hrm.domain.company.entity.NotificationProviders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationProvidersRepository extends JpaRepository<NotificationProviders,Long> {
}
