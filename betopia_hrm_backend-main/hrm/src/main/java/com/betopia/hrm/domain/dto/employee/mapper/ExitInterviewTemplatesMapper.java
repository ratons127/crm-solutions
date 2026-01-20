package com.betopia.hrm.domain.dto.employee.mapper;


import com.betopia.hrm.domain.dto.employee.ExitInterviewTemplatesDTO;
import com.betopia.hrm.domain.employee.entity.ExitInterviewTemplates;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExitInterviewTemplatesMapper {

    ExitInterviewTemplatesDTO toDTO(ExitInterviewTemplates exitInterviewTemplate);

    // List mapping
    List<ExitInterviewTemplatesDTO> toDTOList(List<ExitInterviewTemplates> exitInterviewTemplates);
}
