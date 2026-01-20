package com.betopia.hrm.domain.attendance.specification;

import com.betopia.hrm.domain.attendance.entity.ShiftAssignments;
import com.betopia.hrm.domain.attendance.request.ShowShiftAssignRequest;
import com.betopia.hrm.domain.employee.entity.Employee;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ShowShiftSpecification {

    public static Specification<ShiftAssignments> applicableForShift(
            ShowShiftAssignRequest req, Long supervisorId) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // join employee once
            Join<Object, Object> employeeJoin = root.join("employee", JoinType.LEFT);

            if (req.companyId() != null) {
                predicates.add(cb.equal(employeeJoin.join("company", JoinType.LEFT).get("id"), req.companyId()));
            }

            if (supervisorId != null) {
                predicates.add(cb.equal(root.get("id"), supervisorId));
            }

        /*    if (req.businessUnitId() != null) {
                predicates.add(cb.equal(employeeJoin.join("businessUnit", JoinType.LEFT).get("id"), req.businessUnitId()));
            }
            if (req.workplaceGroupId() != null) {
                predicates.add(cb.equal(employeeJoin.join("workplaceGroup", JoinType.LEFT).get("id"), req.workplaceGroupId()));
            }
            if (req.workplaceId() != null) {
                predicates.add(cb.equal(employeeJoin.join("workplace", JoinType.LEFT).get("id"), req.workplaceId()));
            }
            if (req.departmentId() != null) {
                predicates.add(cb.equal(employeeJoin.join("department", JoinType.LEFT).get("id"), req.departmentId()));
            }
            if (req.teamId() != null) {
                predicates.add(cb.equal(employeeJoin.join("team", JoinType.LEFT).get("id"), req.teamId()));
            }*/

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
