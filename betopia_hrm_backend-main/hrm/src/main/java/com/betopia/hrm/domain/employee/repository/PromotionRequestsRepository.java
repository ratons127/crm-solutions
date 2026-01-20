package com.betopia.hrm.domain.employee.repository;


import com.betopia.hrm.domain.employee.entity.PromotionRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRequestsRepository extends JpaRepository<PromotionRequests, Long> {
}
