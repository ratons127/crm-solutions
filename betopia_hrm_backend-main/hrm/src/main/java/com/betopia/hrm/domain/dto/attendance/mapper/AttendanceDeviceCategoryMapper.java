package com.betopia.hrm.domain.dto.attendance.mapper;

import com.betopia.hrm.domain.attendance.entity.AttendanceDeviceCategory;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceCategoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendanceDeviceCategoryMapper {

    AttendanceDeviceCategoryDTO toDTO(AttendanceDeviceCategory attendanceDeviceCategory);

    List<AttendanceDeviceCategoryDTO> toDTOList(List<AttendanceDeviceCategory> attendanceDeviceCategories);
}
