package com.betopia.hrm.domain.dto.attendance.mapper;


import com.betopia.hrm.domain.attendance.entity.AttendanceSummary;
import com.betopia.hrm.domain.dto.attendance.AttendanceSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendanceSummaryMapper {

    AttendanceSummaryDTO toDTO(AttendanceSummary attendanceSummary);

    // List mapping
    List<AttendanceSummaryDTO> toDTOList(List<AttendanceSummary> attendancePolicies);
}
