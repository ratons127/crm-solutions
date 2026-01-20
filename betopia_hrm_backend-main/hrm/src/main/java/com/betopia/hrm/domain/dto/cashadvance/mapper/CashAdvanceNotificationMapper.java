package com.betopia.hrm.domain.dto.cashadvance.mapper;

import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceNotifications;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceNotificationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CashAdvanceNotificationMapper {

    @Mapping(source = "cashAdvanceApproval.id", target = "cashAdvanceApprovalId")
    CashAdvanceNotificationDTO toDTO(CashAdvanceNotifications cashAdvanceNotifications);
    List<CashAdvanceNotificationDTO> toDTOList(List<CashAdvanceNotifications> cashAdvanceNotifications);
}
