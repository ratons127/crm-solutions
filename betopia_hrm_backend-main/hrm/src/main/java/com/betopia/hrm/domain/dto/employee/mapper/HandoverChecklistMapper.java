package com.betopia.hrm.domain.dto.employee.mapper;

import com.betopia.hrm.domain.dto.employee.HandoverChecklistDTO;
import com.betopia.hrm.domain.employee.entity.HandoverChecklist;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface HandoverChecklistMapper {

    HandoverChecklistDTO toDTO(HandoverChecklist handoverChecklist);

    // List mapping
    List<HandoverChecklistDTO> toDTOList(List<HandoverChecklist> handoverChecklists);
}
