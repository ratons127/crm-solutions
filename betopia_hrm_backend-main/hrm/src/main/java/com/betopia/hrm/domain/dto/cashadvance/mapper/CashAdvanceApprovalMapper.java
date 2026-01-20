package com.betopia.hrm.domain.dto.cashadvance.mapper;

import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceApproval;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceApprovalDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CashAdvanceApprovalMapper {

    @Mapping(source = "advanceCashRequest.id", target = "advanceCashRequestId")
    @Mapping(source = "employee.id", target = "employeeId")

    CashAdvanceApprovalDTO toDTO(CashAdvanceApproval cashAdvanceApproval);
    List<CashAdvanceApprovalDTO> toDTOList(List<CashAdvanceApproval> cashAdvanceApprovals);
}
