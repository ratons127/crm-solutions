package com.betopia.hrm.domain.dto.employee.mapper;

import com.betopia.hrm.domain.dto.employee.FinalSettlementDTO;
import com.betopia.hrm.domain.employee.entity.FinalSettlement;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FinalSettlementMapper {

    FinalSettlementDTO toDTO(FinalSettlement finalSettlement);

    // List mapping
    List<FinalSettlementDTO> toDTOList(List<FinalSettlement> finalSettlements);
}
