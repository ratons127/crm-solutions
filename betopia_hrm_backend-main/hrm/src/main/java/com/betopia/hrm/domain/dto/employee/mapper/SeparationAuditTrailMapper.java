package com.betopia.hrm.domain.dto.employee.mapper;

import com.betopia.hrm.domain.dto.employee.SeparationAuditTrailDTO;
import com.betopia.hrm.domain.employee.entity.SeparationAuditTrail;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeparationAuditTrailMapper {

    SeparationAuditTrailDTO toDTO(SeparationAuditTrail separationAuditTrail);

    // List mapping
    List<SeparationAuditTrailDTO> toDTOList(List<SeparationAuditTrail> separationAuditTrails);
}
