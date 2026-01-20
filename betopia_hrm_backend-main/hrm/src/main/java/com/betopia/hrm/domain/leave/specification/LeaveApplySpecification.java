package com.betopia.hrm.domain.leave.specification;

import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.users.entity.Role;
import com.betopia.hrm.domain.users.entity.User;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public class LeaveApplySpecification {

    public static Specification<LeaveApprovals> globalSearch(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty())
                return cb.conjunction();
            String lower = "%" + keyword.toLowerCase() + "%";

            Join<LeaveApprovals, LeaveRequest> leaveRequestJoin = root.join("leaveRequest", JoinType.LEFT);
            Join<LeaveRequest, LeaveType> leaveTypeJoin = leaveRequestJoin.join("leaveType", JoinType.LEFT);
            Join<LeaveApprovals, Employee> employeeJoin = root.join("employee", JoinType.LEFT);
            Join<Employee, Designation> designationJoin = employeeJoin.join("designation", JoinType.LEFT);


            return cb.or(
                    cb.like(safeLower(cb, root.get("remarks")), lower),
                    cb.like(safeLower(cb, root.get("level")), lower),
                    cb.like(safeLower(cb, root.get("leaveStatus")), lower),

                    cb.like(safeLower(cb, employeeJoin.get("firstName")), lower),
                    cb.like(safeLower(cb, employeeJoin.get("lastName")), lower),
                    cb.like(safeLower(cb, employeeJoin.get("employeeSerialId")), lower),

                    cb.like(safeLower(cb, leaveRequestJoin.get("reason")), lower),
                    cb.like(safeLower(cb, leaveTypeJoin.get("name")), lower),

                    cb.like(safeLower(cb, designationJoin.get("name")), lower)

            );
        };
    }

    private static Expression<String> safeLower(CriteriaBuilder cb, Path<?> path) {
        if (path.getJavaType() == String.class) {
            return cb.lower(path.as(String.class));
        } else {
            return cb.lower(cb.function("text", String.class, path)); // PostgreSQL-safe
        }
    }

}
