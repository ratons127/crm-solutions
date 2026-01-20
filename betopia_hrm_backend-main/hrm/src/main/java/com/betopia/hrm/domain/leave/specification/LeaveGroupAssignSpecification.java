package com.betopia.hrm.domain.leave.specification;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeaveGroupAssign;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;


public class LeaveGroupAssignSpecification {

    public static Specification<LeaveGroupAssign> applicableForEmployee(Employee employee) {
        Long companyId = employee.getCompany() != null ? employee.getCompany().getId() : null;
        Long businessUnitId = employee.getBusinessUnit() != null ? employee.getBusinessUnit().getId() : null;
        Long workplaceGroupId = employee.getWorkplaceGroup() != null ? employee.getWorkplaceGroup().getId() : null;
        Long workplaceId = employee.getWorkplace() != null ? employee.getWorkplace().getId() : null;
        Long departmentId = employee.getDepartment() != null ? employee.getDepartment().getId() : null;
        Long teamId = employee.getTeam() != null ? employee.getTeam().getId() : null;

        return (root, query, cb) -> {
            Predicate companyMatch = cb.equal(root.get("company").get("id"), companyId);

            Predicate businessUnitMatch = cb.or(
                    cb.isNull(root.get("businessUnit")),
                    cb.equal(root.get("businessUnit").get("id"), businessUnitId)
            );

            Predicate workplaceGroupMatch = cb.or(
                    cb.isNull(root.get("workplaceGroup")),
                    cb.equal(root.get("workplaceGroup").get("id"), workplaceGroupId)
            );

            Predicate workplaceMatch = cb.or(
                    cb.isNull(root.get("workplace")),
                    cb.equal(root.get("workplace").get("id"), workplaceId)
            );

            Predicate departmentMatch = cb.or(
                    cb.isNull(root.get("department")),
                    cb.equal(root.get("department").get("id"), departmentId)
            );

            Predicate teamMatch = cb.or(
                    cb.isNull(root.get("team")),
                    cb.equal(root.get("team").get("id"), teamId)
            );

            Expression<Integer> specificity = cb.sum(
                    cb.sum(
                            cb.sum(
                                    cb.sum(
                                            cb.<Integer>selectCase().when(cb.isNotNull(root.get("team")), 6).otherwise(0),
                                            cb.<Integer>selectCase().when(cb.isNotNull(root.get("department")), 5).otherwise(0)
                                    ),
                                    cb.<Integer>selectCase().when(cb.isNotNull(root.get("workplace")), 4).otherwise(0)
                            ),
                            cb.<Integer>selectCase().when(cb.isNotNull(root.get("workplaceGroup")), 3).otherwise(0)
                    ),
                    cb.<Integer>selectCase().when(cb.isNotNull(root.get("businessUnit")), 2).otherwise(0)
            );

            query.orderBy(cb.desc(specificity), cb.desc(root.get("id")));

            return cb.and(companyMatch, businessUnitMatch, workplaceGroupMatch,
                          workplaceMatch, departmentMatch, teamMatch);
        };
    }

    public static Specification<LeaveGroupAssign> hasLeaveType(LeaveType leaveType) {
        return (root, query, cb) -> {
            if (leaveType == null) return cb.conjunction(); // skip filter if null

            // Create a subquery on LeavePolicy
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<LeavePolicy> policyRoot = subquery.from(LeavePolicy.class);

            // Select LeaveGroupAssign IDs that match the given LeaveType
            subquery.select(policyRoot.get("leaveGroupAssign").get("id"))
                    .where(cb.equal(policyRoot.get("leaveType").get("id"), leaveType.getId()));

            // Apply "id IN (subquery)"
            return cb.in(root.get("id")).value(subquery);
        };
    }
}
