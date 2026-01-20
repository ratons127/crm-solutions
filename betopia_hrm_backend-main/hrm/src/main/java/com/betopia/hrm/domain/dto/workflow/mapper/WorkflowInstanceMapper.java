package com.betopia.hrm.domain.dto.workflow.mapper;

import com.betopia.hrm.domain.dto.workflow.WorkflowInstanceDTO;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WorkflowInstanceMapper {

    @Mapping(target = "moduleId", source = "module.id")
    @Mapping(target = "templateId", source = "template.id")
    @Mapping(target = "currentStageId", source = "currentStage.id")
    WorkflowInstanceDTO toDTO(WorkflowInstance entity);
}
