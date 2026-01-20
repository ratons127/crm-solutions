package com.betopia.hrm.domain.workflow.request;

import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ModuleRequest (
        Long id,

        @NotNull(message = "Module name cannot be null")
        @NotBlank(message = "Module name cannot be blank")
        String moduleName,

        String moduleCode,
        Boolean status,
        List<WorkflowInstance> instances
){
}
