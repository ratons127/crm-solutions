package com.betopia.hrm.domain.dto.workflow.mapper;

import com.betopia.hrm.domain.dto.workflow.WorkflowNotificationDTO;
import com.betopia.hrm.domain.workflow.entity.WorkflowNotification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WorkflowNotificationMapper {

    @Mapping(target = "instanceId", source = "instance.id")
    WorkflowNotificationDTO toDTO(WorkflowNotification entity);
}
