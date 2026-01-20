package com.betopia.hrm.domain.attendance.specification;

import com.betopia.hrm.domain.attendance.entity.AttendanceApproval;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public class ManualApprovalSpecification {

    public static Specification<AttendanceApproval> globalSearch(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();
            String lower = "%" + keyword.toLowerCase() + "%";

            return cb.or(
                    cb.like(safeLower(cb, root.get("employeeId")), lower),
                    cb.like(safeLower(cb, root.get("employeeName")), lower),
                    cb.like(safeLower(cb, root.get("employeeSerialId")), lower),
                    cb.like(safeLower(cb, root.get("date")), lower),
                    cb.like(safeLower(cb, root.get("inTime")), lower),
                    cb.like(safeLower(cb, root.get("outTime")), lower),
                    cb.like(safeLower(cb, root.get("adjustmentType")), lower),
                    cb.like(safeLower(cb, root.get("manualApprovalStatus")), lower),
                    cb.like(safeLower(cb, root.get("reason")), lower)
            );
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
