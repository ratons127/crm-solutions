package com.betopia.hrm.controllers.rest.v1.workflow;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import com.betopia.hrm.domain.workflow.request.ApproveRequest;
import com.betopia.hrm.domain.workflow.request.StartWorkflowRequest;
import com.betopia.hrm.services.workflow.approvalaction.ApprovalService;
import com.betopia.hrm.services.workflow.workflowinstance.WorkflowInstanceService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/workflows")
@Tag(
        name = "Workflow -> Workflows",
        description = "APIs for workflows"
)
public class WorkflowController {

    private final WorkflowInstanceService instanceService;
    private final ApprovalService approvalService;

    public WorkflowController(
            WorkflowInstanceService instanceService,
            ApprovalService approvalService
    ) {
        this.instanceService = instanceService;
        this.approvalService = approvalService;
    }

    @PostMapping("/start")
    @Operation(summary = "Workflow start", description = "Start workflow request")
    public ResponseEntity<GlobalResponse> start(
            @RequestBody StartWorkflowRequest request
    ) {
        WorkflowInstance inst = instanceService.startWorkflow(request);
        return ResponseBuilder.ok(inst, "Workflow started successfully");
    }

    @PostMapping("/approve")
    public ResponseEntity<Void> approve(
            @RequestBody ApproveRequest request
    ) {
        approvalService.approve(request);
        return ResponseEntity.ok().build();
    }
}
