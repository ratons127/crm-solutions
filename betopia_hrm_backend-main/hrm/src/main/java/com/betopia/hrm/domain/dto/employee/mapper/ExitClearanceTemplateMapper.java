package com.betopia.hrm.domain.dto.employee.mapper;


import com.betopia.hrm.domain.dto.employee.ExitClearanceTemplateDTO;
import com.betopia.hrm.domain.employee.entity.ExitClearanceTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExitClearanceTemplateMapper {
    ExitClearanceTemplateDTO toDTO(ExitClearanceTemplate exitClearanceTemplate);

    // List mapping
    List<ExitClearanceTemplateDTO> toDTOList(List<ExitClearanceTemplate> exitClearanceTemplates);
}
