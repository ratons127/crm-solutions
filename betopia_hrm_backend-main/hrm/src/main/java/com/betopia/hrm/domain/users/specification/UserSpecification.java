package com.betopia.hrm.domain.users.specification;

import com.betopia.hrm.domain.users.entity.Role;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.entity.Workplace;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecification {

    // For global keyword search across all fields
    public static Specification<User> globalSearch(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) return cb.conjunction();
            String lower = "%" + keyword.toLowerCase() + "%";

//            // 🔹 join relational fields
//            Join<User, Role> roleJoin = root.join("roleJoin", JoinType.LEFT);

            return cb.or(
                    cb.like(safeLower(cb, root.get("name")), lower),
                    cb.like(safeLower(cb, root.get("employeeSerialId")), lower),
                    cb.like(safeLower(cb, root.get("email")), lower),
                    cb.like(safeLower(cb, root.get("phone")), lower)
//                    cb.like(safeLower(cb, roleJoin.get("name")), lower)
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
