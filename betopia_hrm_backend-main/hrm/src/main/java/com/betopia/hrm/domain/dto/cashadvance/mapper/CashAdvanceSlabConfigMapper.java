package com.betopia.hrm.domain.dto.cashadvance.mapper;

import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceSlabConfig;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceSlabConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CashAdvanceSlabConfigMapper {

    @Mapping(source = "employeeType.id", target = "employeeTypeId")
    @Mapping(source = "company.id", target = "companyId")
    @Mapping(source = "businessUnit.id", target = "businessUnitId")
    @Mapping(source = "workplaceGroup.id", target = "workplaceGroupId")
    @Mapping(source = "workplace.id", target = "workplaceId")
    @Mapping(source = "advancePercent", target = "advancePercent")
    @Mapping(source = "serviceChargeAmount", target = "serviceChargeAmount")
    CashAdvanceSlabConfigDTO toDTO(CashAdvanceSlabConfig cashAdvanceSlabConfig);
    List<CashAdvanceSlabConfigDTO> toDTOList(List<CashAdvanceSlabConfig> cashAdvanceSlabConfigs);
}
