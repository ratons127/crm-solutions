package com.betopia.hrm.domain.dto.employee.mapper;


import com.betopia.hrm.domain.dto.employee.ExitClearanceItemDTO;
import com.betopia.hrm.domain.employee.entity.ExitClearanceItem;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ExitClearanceItemMapper {

    ExitClearanceItemDTO toDTO(ExitClearanceItem exitClearanceItem);

    // List mapping
    List<ExitClearanceItemDTO> toDTOList(List<ExitClearanceItem> exitClearanceItems);
}
