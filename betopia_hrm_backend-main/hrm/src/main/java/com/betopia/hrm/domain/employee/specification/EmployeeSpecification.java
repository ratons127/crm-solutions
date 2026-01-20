package com.betopia.hrm.domain.employee.specification;

import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.employee.entity.Designation;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.request.EmployeeShiftAssignRequest;
import com.betopia.hrm.domain.users.entity.Workplace;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class EmployeeSpecification {

    public static Specification<Employee> applicableForEmployee(EmployeeShiftAssignRequest req) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (req.employeeId() != null) {
                predicates.add(cb.equal(root.get("id"), req.employeeId()));
            }
            if (req.companyId() != null) {
                predicates.add(cb.equal(root.get("company").get("id"), req.companyId()));
            }


            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<Employee> globalSearch(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();
            String lower = "%" + keyword.toLowerCase() + "%";

            // 🔹 join relational fields
            Join<Employee, Designation> desigJoin = root.join("designation", JoinType.LEFT);
            Join<Employee, BusinessUnit> buJoin = root.join("businessUnit", JoinType.LEFT);
            Join<Employee, WorkplaceGroup> wpgJoin = root.join("workplaceGroup", JoinType.LEFT);
            Join<Employee, Workplace> wpJoin = root.join("workplace", JoinType.LEFT);

            return cb.or(
                    cb.like(safeLower(cb, root.get("firstName")), lower),
                    cb.like(safeLower(cb, root.get("lastName")), lower),
                    cb.like(safeLower(cb, root.get("employeeSerialId")), lower),
                    cb.like(safeLower(cb, root.get("email")), lower),
                    cb.like(safeLower(cb, root.get("phone")), lower),
                    cb.like(safeLower(cb, root.get("gender")), lower),
                    cb.like(safeLower(cb, desigJoin.get("name")), lower),
                    cb.like(safeLower(cb, buJoin.get("name")), lower),
                    cb.like(safeLower(cb, wpgJoin.get("name")), lower),
                    cb.like(safeLower(cb, wpJoin.get("name")), lower)
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
