package com.betopia.hrm.domain.dto.cashadvance.mapper;

import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashConfig;
import com.betopia.hrm.domain.dto.cashadvance.AdvanceCashConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdvanceCashConfigMapper {

    @Mapping(source = "employeeType.id", target = "employeeTypeId")
    AdvanceCashConfigDTO toDTO(AdvanceCashConfig advanceCashConfig);
    List<AdvanceCashConfigDTO> toDTOList(List<AdvanceCashConfig> advanceCashConfigs);
}
