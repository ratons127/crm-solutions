package com.betopia.hrm.domain.workflow.request;

import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import com.betopia.hrm.domain.workflow.entity.WorkflowStage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WorkflowTemplateRequest(
        Long id,

        @NotNull(message = "Template name cannot be null")
        @NotBlank(message = "Template name cannot be blank")
        String templateName,

        String description,
        Boolean isDefault,
        Boolean status,

        @NotNull(message = "Module ID cannot be null")
        Long moduleId,

        List<WorkflowStage> stages,
        List<WorkflowInstance> instances
) {
}
