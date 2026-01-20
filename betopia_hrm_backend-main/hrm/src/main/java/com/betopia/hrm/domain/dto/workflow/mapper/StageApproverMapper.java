package com.betopia.hrm.domain.dto.workflow.mapper;

import com.betopia.hrm.domain.dto.workflow.StageApproverDTO;
import com.betopia.hrm.domain.workflow.entity.StageApprover;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StageApproverMapper {

    @Mapping(target = "stageId", source = "stage.id")
    StageApproverDTO toDTO(StageApprover entity);
}
