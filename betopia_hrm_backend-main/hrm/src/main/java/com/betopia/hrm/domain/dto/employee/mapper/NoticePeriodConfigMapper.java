package com.betopia.hrm.domain.dto.employee.mapper;


import com.betopia.hrm.domain.dto.employee.NoticePeriodConfigDTO;
import com.betopia.hrm.domain.employee.entity.NoticePeriodConfig;
import org.mapstruct.ReportingPolicy;

import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NoticePeriodConfigMapper {

    NoticePeriodConfigDTO toDTO(NoticePeriodConfig noticePeriodConfig);

    // List mapping
    List<NoticePeriodConfigDTO> toDTOList(List<NoticePeriodConfig> noticePeriodConfigs);

}
