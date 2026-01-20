package com.betopia.hrm.domain.dto.cashadvance.mapper;

import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashRequest;
import com.betopia.hrm.domain.dto.cashadvance.AdvanceCashRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AdvanceCashRequestMappers {

    @Mapping(source = "requestedAmount", target = "requestedAmount")
    @Mapping(source = "serviceChargeAmount", target = "serviceChargeAmount")
    @Mapping(source = "deductedAmount", target = "deductedAmount")
    AdvanceCashRequestDTO toDTO(AdvanceCashRequest advanceCashRequest);
    List<AdvanceCashRequestDTO> toDTOList(List<AdvanceCashRequest> advanceCashRequests);
}
