package com.betopia.hrm.domain.dto.workflow.mapper;

import com.betopia.hrm.domain.dto.workflow.WorkflowStageDTO;
import com.betopia.hrm.domain.workflow.entity.WorkflowStage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WorkflowStageMapper {

    @Mapping(target = "templateId", source = "template.id")
    WorkflowStageDTO toDTO(WorkflowStage entity);
}
