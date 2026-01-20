package com.betopia.hrm.domain.dto.employee.mapper;


import com.betopia.hrm.domain.dto.employee.ExitInterviewDTO;
import com.betopia.hrm.domain.employee.entity.ExitInterview;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExitInterviewMapper {

    ExitInterviewDTO toDTO(ExitInterview exitInterview);

    // List mapping
    List<ExitInterviewDTO> toDTOList(List<ExitInterview> exitInterviews);
}
