package com.betopia.hrm.domain.dto.workflow.mapper;

import com.betopia.hrm.domain.dto.workflow.ApprovalActionDTO;
import com.betopia.hrm.domain.workflow.entity.ApprovalAction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ApprovalActionMapper {

    @Mapping(target = "instanceId", source = "instance.id")
    @Mapping(target = "instanceStageId", source = "instanceStage.id")
    @Mapping(target = "stageId", source = "stage.id")
    ApprovalActionDTO toDTO(ApprovalAction entity);
}
