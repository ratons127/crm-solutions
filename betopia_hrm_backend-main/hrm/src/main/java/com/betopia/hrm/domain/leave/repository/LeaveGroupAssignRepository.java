package com.betopia.hrm.domain.leave.repository;

import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.Department;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.leave.entity.LeaveGroup;
import com.betopia.hrm.domain.leave.entity.LeaveGroupAssign;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.Workplace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeaveGroupAssignRepository extends JpaRepository<LeaveGroupAssign, Long>, JpaSpecificationExecutor<LeaveGroupAssign> {

    @Query("""
    SELECT COUNT(lga) > 0
    FROM LeaveGroupAssign lga
    WHERE lga.company = :company
      AND (:businessUnit IS NULL OR lga.businessUnit = :businessUnit)
      AND (:workplaceGroup IS NULL OR lga.workplaceGroup = :workplaceGroup)
      AND (:workplace IS NULL OR lga.workplace = :workplace)
      AND (:department IS NULL OR lga.department = :department)
      AND (:team IS NULL OR lga.team = :team)
      AND lga.leaveType = :leaveType
      AND (:leaveGroup IS NULL OR lga.leaveGroup = :leaveGroup)
""")
    boolean existsDuplicateOnCreate(
            @Param("company") Company company,
            @Param("businessUnit") BusinessUnit businessUnit,
            @Param("workplaceGroup") WorkplaceGroup workplaceGroup,
            @Param("workplace") Workplace workplace,
            @Param("department") Department department,
            @Param("team") Team team,
            @Param("leaveType") LeaveType leaveType,
            @Param("leaveGroup") LeaveGroup leaveGroup
    );

    @Query("""
                SELECT COUNT(lga) > 0
                FROM LeaveGroupAssign lga
                WHERE lga.company = :company
                  AND (:businessUnit IS NULL OR lga.businessUnit = :businessUnit)
                  AND (:workplaceGroup IS NULL OR lga.workplaceGroup = :workplaceGroup)
                  AND (:workplace IS NULL OR lga.workplace = :workplace)
                  AND (:department IS NULL OR lga.department = :department)
                  AND (:team IS NULL OR lga.team = :team)
                  AND lga.leaveType = :leaveType
                  AND (:leaveGroup IS NULL OR lga.leaveGroup = :leaveGroup)
                  AND lga.id <> :id
            """)
    boolean existsDuplicateOnUpdate(
            @Param("company") Company company,
            @Param("businessUnit") BusinessUnit businessUnit,
            @Param("workplaceGroup") WorkplaceGroup workplaceGroup,
            @Param("workplace") Workplace workplace,
            @Param("department") Department department,
            @Param("team") Team team,
            @Param("leaveType") LeaveType leaveType,
            @Param("leaveGroup") LeaveGroup leaveGroup,
            @Param("id") Long id
    );
}
