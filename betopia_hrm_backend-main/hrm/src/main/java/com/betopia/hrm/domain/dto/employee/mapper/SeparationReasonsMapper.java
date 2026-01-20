package com.betopia.hrm.domain.dto.employee.mapper;

import com.betopia.hrm.domain.dto.employee.SeparationReasonsDTO;
import com.betopia.hrm.domain.employee.entity.SeparationReasons;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SeparationReasonsMapper {

    SeparationReasonsDTO toDTO(SeparationReasons separationReason);

    // List mapping
    List<SeparationReasonsDTO> toDTOList(List<SeparationReasons> separationReasons);
}
