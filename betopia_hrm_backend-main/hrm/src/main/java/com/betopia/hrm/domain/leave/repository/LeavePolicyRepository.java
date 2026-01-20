package com.betopia.hrm.domain.leave.repository;

import com.betopia.hrm.domain.leave.entity.LeaveGroupAssign;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, Long> {

    Optional<LeavePolicy> findByLeaveType(LeaveType leaveType);

    List<LeavePolicy> findByLeaveGroupAssign(LeaveGroupAssign leaveGroupAssign);

    @Query("SELECT p FROM LeavePolicy p WHERE p.leaveGroupAssign.id IN :assignIds")
    List<LeavePolicy> findByLeaveGroupAssignIds(@Param("assignIds") List<Long> assignIds);

    List<LeavePolicy> findByLeaveTypeAndLeaveGroupAssign(LeaveType leaveType, LeaveGroupAssign leaveGroupAssign);

    @Query("""
        SELECT CASE WHEN COUNT(lp) > 0 THEN TRUE ELSE FALSE END
        FROM LeavePolicy lp
        WHERE lp.leaveType.id = :leaveTypeId
          AND lp.leaveGroupAssign.id = :leaveGroupAssignId
          AND (
                (:employeeTypeId IS NOT NULL AND lp.employeeTypeId = :employeeTypeId)
                OR
                (:employeeTypeId IS NULL AND lp.tenureRequiredDays = :tenureRequiredDays AND lp.employeeTypeId IS NULL)
              )
          AND (:id IS NULL OR lp.id <> :id)
    """)
    boolean existsDuplicate(@Param("leaveTypeId") Long leaveTypeId,
                            @Param("leaveGroupAssignId") Long leaveGroupAssignId,
                            @Param("employeeTypeId") Long employeeTypeId,
                            @Param("tenureRequiredDays") Integer tenureRequiredDays,
                            @Param("id") Long id);
}
