package com.betopia.hrm.domain.dto.workflow.mapper;

import com.betopia.hrm.domain.dto.workflow.WorkflowEscalationDTO;
import com.betopia.hrm.domain.workflow.entity.WorkflowEscalation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WorkflowEscalationMapper {

    @Mapping(target = "instanceId", source = "instance.id")
    @Mapping(target = "instanceStageId", source = "instanceStage.id")
    WorkflowEscalationDTO toDTO(WorkflowEscalation entity);
}
