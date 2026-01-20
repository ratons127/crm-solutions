package com.betopia.hrm.domain.dto.leave.mapper;

import com.betopia.hrm.domain.dto.leave.LeaveNotificationsDTO;
import com.betopia.hrm.domain.leave.entity.LeaveNotifications;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LeaveNotificationsMapper {

    LeaveNotificationsDTO toDTO(LeaveNotifications leaveNotification);

    // List mapping
    List<LeaveNotificationsDTO> toDTOList(List<LeaveNotifications> leaveNotifications);
}
