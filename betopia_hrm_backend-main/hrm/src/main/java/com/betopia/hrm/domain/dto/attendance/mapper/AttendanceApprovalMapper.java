package com.betopia.hrm.domain.dto.attendance.mapper;


import com.betopia.hrm.domain.attendance.entity.AttendanceApproval;
import com.betopia.hrm.domain.attendance.entity.AttendanceSummary;
import com.betopia.hrm.domain.dto.attendance.AttendanceApprovalDTO;
import com.betopia.hrm.domain.dto.attendance.AttendanceSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendanceApprovalMapper {

    AttendanceApprovalDTO toDTO(AttendanceApproval attendanceApproval);

    // List mapping
    List<AttendanceApprovalDTO> toDTOList(List<AttendanceApproval> attendanceApprovals);
}
