package com.betopia.hrm.domain.dto.attendance.mapper;

import com.betopia.hrm.domain.attendance.entity.AttendancePolicy;
import com.betopia.hrm.domain.dto.attendance.AttendancePolicyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendancePolicyMapper {

    AttendancePolicyDTO toDTO(AttendancePolicy attendancePolicy);

    // List mapping
    List<AttendancePolicyDTO> toDTOList(List<AttendancePolicy> attendancePolicies);
}
