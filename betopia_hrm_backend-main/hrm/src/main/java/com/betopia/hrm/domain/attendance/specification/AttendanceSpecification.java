package com.betopia.hrm.domain.attendance.specification;

import com.betopia.hrm.domain.attendance.entity.ManualAttendance;
import com.betopia.hrm.domain.employee.entity.Employee;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class AttendanceSpecification {

    public static Specification<ManualAttendance> globalSearch(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();

            String lower = "%" + keyword.toLowerCase(Locale.ROOT) + "%";

            // Optional: join with Employee to search employee name or serial
            Subquery<Long> empSub = query.subquery(Long.class);
            Root<Employee> empRoot = empSub.from(Employee.class);
            empSub.select(empRoot.get("id"))
                    .where(cb.equal(empRoot.get("id"), root.get("employeeId")),
                            cb.or(
                                    cb.like(safeLower(cb, empRoot.get("firstName")), lower),
                                    cb.like(safeLower(cb, empRoot.get("lastName")), lower),
                                    cb.like(safeLower(cb, empRoot.get("employeeSerialId")), lower)
                            )
                    );

            // ManualAttendance fields search
            Predicate attendanceSearch = cb.or(
                    cb.like(safeLower(cb, root.get("employeeId")), lower),
                    cb.like(safeLower(cb, root.get("attendanceDate")), lower),
                    cb.like(safeLower(cb, root.get("inTime")), lower),
                    cb.like(safeLower(cb, root.get("outTime")), lower),
                    cb.like(safeLower(cb, root.get("adjustmentType")), lower),
                    cb.like(safeLower(cb, root.get("submittedAt")), lower),
                    cb.like(safeLower(cb, root.get("manualAttendanceStatus")), lower),
                    cb.like(safeLower(cb, root.get("reason")), lower)
            );

            // Combine with Employee subquery
            return cb.or(attendanceSearch, cb.exists(empSub));
        };
    }

    private static Expression<String> safeLower(CriteriaBuilder cb, Path<?> path) {
        if (path.getJavaType() == String.class) {
            return cb.lower(path.as(String.class));
        } else {
            // PostgreSQL-safe cast to text for enums, dates, booleans, numbers
            return cb.lower(cb.function("text", String.class, path));
        }
    }
}