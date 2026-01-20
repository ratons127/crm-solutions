package com.betopia.hrm.domain.leave.repository;


import com.betopia.hrm.domain.leave.entity.LeaveGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveGroupRepository extends JpaRepository<LeaveGroup, Long> {
}
