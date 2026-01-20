package com.betopia.hrm.domain.dto.attendance.mapper;

import com.betopia.hrm.domain.attendance.entity.ShiftAssignments;
import com.betopia.hrm.domain.dto.attendance.ShiftAssignmentsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ShiftAssignmentsMapper {
    @Mapping(source = "id", target = "id")
    @Mapping(source = "employee", target = "employee")
    @Mapping(source = "shift", target = "shift")
    @Mapping(source = "effectiveFrom", target = "effectiveFrom")
    @Mapping(source = "effectiveTo", target = "effectiveTo")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "assignmentSource", target = "assignmentSource")
    @Mapping(source = "assignedBy", target = "assignedBy")
    @Mapping(source = "assignedAt", target = "assignedAt")
    ShiftAssignmentsDTO toDTO(ShiftAssignments shiftAssignment);

    // List mapping
    List<ShiftAssignmentsDTO> toDTOList(List<ShiftAssignments> shiftAssignments);
}
