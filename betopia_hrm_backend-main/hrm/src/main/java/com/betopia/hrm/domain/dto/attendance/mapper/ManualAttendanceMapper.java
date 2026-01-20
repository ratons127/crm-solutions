package com.betopia.hrm.domain.dto.attendance.mapper;

import com.betopia.hrm.domain.attendance.entity.ManualAttendance;
import com.betopia.hrm.domain.dto.attendance.ManualAttendanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ManualAttendanceMapper {

    ManualAttendanceDTO toDTO(ManualAttendance manualAttendance);

    // List mapping
    List<ManualAttendanceDTO> toDTOList(List<ManualAttendance> manualAttendances);
}
