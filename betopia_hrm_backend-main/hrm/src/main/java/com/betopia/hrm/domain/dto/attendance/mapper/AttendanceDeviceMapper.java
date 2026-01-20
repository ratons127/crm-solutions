package com.betopia.hrm.domain.dto.attendance.mapper;

import com.betopia.hrm.domain.attendance.entity.AttendanceDevice;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendanceDeviceMapper {

    AttendanceDeviceDTO toDTO(AttendanceDevice attendanceDevice);

    List<AttendanceDeviceDTO> toDTOList(List<AttendanceDevice> attendanceDevices);
}
