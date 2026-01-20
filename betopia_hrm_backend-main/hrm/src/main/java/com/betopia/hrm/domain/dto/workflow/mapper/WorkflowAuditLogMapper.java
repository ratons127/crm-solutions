package com.betopia.hrm.domain.dto.workflow.mapper;

import com.betopia.hrm.domain.dto.workflow.WorkflowAuditLogDTO;
import com.betopia.hrm.domain.workflow.entity.WorkflowAuditLog;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WorkflowAuditLogMapper {

    @Mapping(target = "instanceId", source = "instance.id")
    WorkflowAuditLogDTO toDTO(WorkflowAuditLog entity);
}
