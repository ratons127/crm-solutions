package com.betopia.hrm.domain.workflow.request;

public record StartWorkflowRequest(
        Long moduleId,
        Long templateId,
        Long referenceId,
        String referenceNumber,
        Long initiatedBy,
        String remarks
) {}
