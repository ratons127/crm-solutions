package com.betopia.hrm.domain.dto.workflow.mapper;

import com.betopia.hrm.domain.dto.workflow.WorkflowTemplateDTO;
import com.betopia.hrm.domain.workflow.entity.WorkflowTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface WorkflowTemplateMapper {

    @Mapping(target = "moduleId", source = "module.id")
    WorkflowTemplateDTO toDTO(WorkflowTemplate entity);
}
