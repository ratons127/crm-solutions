package com.betopia.hrm.domain.leave.repository;

import com.betopia.hrm.domain.leave.entity.LeaveNotifications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaveNotificationsRepository extends JpaRepository<LeaveNotifications, Long> {

    Optional<LeaveNotifications> findByLeaveApproval_LeaveRequest_Id(Long leaveRequestId);

//    Optional<LeaveNotifications> findTopByLeaveApproval_LeaveRequest_IdOrderByCreatedAtDesc(Long leaveRequestId);


}
