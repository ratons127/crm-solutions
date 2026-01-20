package com.betopia.hrm.domain.dto.workflow.mapper;

import com.betopia.hrm.domain.dto.workflow.WorkflowInstanceStageDTO;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstanceStage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WorkflowInstanceStageMapper {

    @Mapping(target = "instanceId", source = "instance.id")
    @Mapping(target = "stageId", source = "stage.id")
    WorkflowInstanceStageDTO toDTO(WorkflowInstanceStage entity);
}
