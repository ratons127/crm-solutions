package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.TransferRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRequestsRepository extends JpaRepository<TransferRequests, Long> {
}
