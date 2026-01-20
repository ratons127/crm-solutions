package com.betopia.hrm.domain.attendance.repository;

import com.betopia.hrm.domain.attendance.entity.AttendanceDeviceCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttendanceDeviceCategoryRepository extends JpaRepository<AttendanceDeviceCategory, Long> {
}
