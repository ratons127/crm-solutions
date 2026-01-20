package com.betopia.hrm.domain.attendance.repository;

import com.betopia.hrm.domain.attendance.entity.AttendancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendancePolicyRepository extends JpaRepository<AttendancePolicy, Long> {

    boolean existsByCompanyId(Long companyId);

}
