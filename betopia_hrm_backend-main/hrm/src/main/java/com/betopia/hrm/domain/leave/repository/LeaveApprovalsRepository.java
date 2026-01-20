package com.betopia.hrm.domain.leave.repository;

import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import com.betopia.hrm.domain.users.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveApprovalsRepository extends JpaRepository<LeaveApprovals, Long> , JpaSpecificationExecutor<LeaveApprovals> {

    List<LeaveApprovals> findByApproverId(Long approverId);



}
