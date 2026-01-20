package com.betopia.hrm.domain.dto.workflow.mapper;

import com.betopia.hrm.domain.dto.workflow.ModuleDTO;
import com.betopia.hrm.domain.workflow.entity.Module;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ModuleMapper {

    ModuleDTO toDTO(Module entity);
}
