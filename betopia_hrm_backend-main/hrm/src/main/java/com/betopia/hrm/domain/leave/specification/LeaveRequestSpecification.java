package com.betopia.hrm.domain.leave.specification;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import com.betopia.hrm.domain.leave.entity.LeaveGroupAssign;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class LeaveRequestSpecification {

    public static Specification<LeaveRequest> globalSearch(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }

            String lower = "%" + keyword.toLowerCase(Locale.ROOT) + "%";

            // Join LeaveType and LeaveGroupAssign
            Join<LeaveRequest, LeaveType> leaveTypeJoin = root.join("leaveType", JoinType.LEFT);
            Join<LeaveRequest, LeaveGroupAssign> leaveGroupJoin = root.join("leaveGroupAssign", JoinType.LEFT);

            // Subquery for Employee
            Subquery<Long> empSub = query.subquery(Long.class);
            Root<Employee> empRoot = empSub.from(Employee.class);
            empSub.select(empRoot.get("id"))
                    .where(cb.equal(empRoot.get("id"), root.get("employeeId")),
                            cb.or(
                                    cb.like(safeLower(cb, empRoot.get("firstName")), lower),
                                    cb.like(safeLower(cb, empRoot.get("lastName")), lower),
                                    cb.like(safeLower(cb, empRoot.get("employeeSerialId")), lower),
                                    cb.like(safeLower(cb, empRoot.get("designation").get("name")), lower)
                            )
                    );

            // LeaveRequest fields search
            Predicate leaveRequestSearch = cb.or(
                    cb.like(safeLower(cb, root.get("reason")), lower),
                    cb.like(safeLower(cb, root.get("justification")), lower),
                    cb.like(safeLower(cb, root.get("status")), lower),
                    cb.like(safeLower(cb, root.get("daysRequested")), lower),
                    cb.like(safeLower(cb, root.get("startDate")), lower),
                    cb.like(safeLower(cb, root.get("endDate")), lower),
                    cb.like(safeLower(cb, root.get("halfDay")), lower)
            );

            // LeaveType and LeaveGroupAssign search
            Predicate joinSearch = cb.or(
                    cb.like(safeLower(cb, leaveTypeJoin.get("name")), lower)
            );

            // Employee search via EXISTS
            Predicate employeeExists = cb.exists(empSub);

            // Combine everything using OR (global search)
            return cb.or(leaveRequestSearch, joinSearch, employeeExists);
        };
    }

    private static Expression<String> safeLower(CriteriaBuilder cb, Path<?> path) {
        if (path.getJavaType() == String.class) {
            return cb.lower(path.as(String.class));
        } else {
            // PostgreSQL-safe cast to text
            return cb.lower(cb.function("text", String.class, path));
        }
    }
}
