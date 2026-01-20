package com.betopia.hrm.services.workflow.workflownotification.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.ApprovalActionDTO;
import com.betopia.hrm.domain.dto.workflow.WorkflowNotificationDTO;
import com.betopia.hrm.domain.dto.workflow.mapper.WorkflowNotificationMapper;
import com.betopia.hrm.domain.workflow.entity.ApprovalAction;
import com.betopia.hrm.domain.workflow.entity.WorkflowInstance;
import com.betopia.hrm.domain.workflow.entity.WorkflowNotification;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowNotificationNotFound;
import com.betopia.hrm.domain.workflow.repository.WorkflowInstanceRepository;
import com.betopia.hrm.domain.workflow.repository.WorkflowNotificationRepository;
import com.betopia.hrm.domain.workflow.request.WorkflowNotificationRequest;
import com.betopia.hrm.services.workflow.workflownotification.WorkflowNotificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Service
public class WorkflowNotificationServiceImpl implements WorkflowNotificationService {

    private final WorkflowNotificationRepository repository;
    private final WorkflowNotificationMapper mapper;
    private final WorkflowInstanceRepository instanceRepository;

    public WorkflowNotificationServiceImpl(
            WorkflowNotificationRepository repository,
            WorkflowNotificationMapper mapper,
            WorkflowInstanceRepository instanceRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.instanceRepository = instanceRepository;
    }

    @Override
    public PaginationResponse<WorkflowNotificationDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<WorkflowNotification> workflowNotificationPage = repository.findAll(pageable);

        PaginationResponse<WorkflowNotificationDTO> response = new PaginationResponse<>();
        response.setData(workflowNotificationPage.getContent().stream()
                .map(mapper::toDTO)
                .toList());
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All workflow notifications fetched successfully");
        response.setLinks(Links.fromPage(workflowNotificationPage, "/workflow-notifications"));
        response.setMeta(Meta.fromPage(workflowNotificationPage, "/workflow-notifications"));

        return response;
    }

    @Override
    public List<WorkflowNotificationDTO> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public WorkflowNotificationDTO getById(Long id) {
        return mapper.toDTO(repository.findById(id)
                .orElseThrow(() -> new WorkflowNotificationNotFound("Workflow notification not found with id: " + id)));
    }

    @Override
    public WorkflowNotificationDTO store(WorkflowNotificationRequest request) {
        WorkflowInstance instance = instanceRepository.findById(request.instanceId())
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance not found with id:" + request.instanceId()));

        WorkflowNotification workflowNotification=new WorkflowNotification();
        workflowNotification.setNotificationType(request.notificationType());
        workflowNotification.setRecipientId(request.recipientId());
        workflowNotification.setSubject(request.subject());
        workflowNotification.setMessage(request.message());
        workflowNotification.setSentAt(request.sentAt());
        workflowNotification.setIsRead(request.isRead());
        workflowNotification.setReadAt(request.readAt());
        workflowNotification.setInstance(instance);

        return mapper.toDTO(repository.save(workflowNotification));
    }

    @Override
    public WorkflowNotificationDTO update(Long id, WorkflowNotificationRequest request) {
        WorkflowNotification notification = repository.findById(id)
                .orElseThrow(() -> new WorkflowNotificationNotFound("Workflow notification not found with id:" + id));

        WorkflowInstance instance = instanceRepository.findById(request.instanceId())
                .orElseThrow(() -> new WorkflowInstanceNotFound("Workflow instance not found with id:" + request.instanceId()));

        updateIfNotNull(request.notificationType(), notification::setNotificationType);
        updateIfNotNull(request.recipientId(), notification::setRecipientId);
        updateIfNotNull(request.subject(), notification::setSubject);
        updateIfNotNull(request.message(), notification::setMessage);
        updateIfNotNull(request.sentAt(), notification::setSentAt);
        updateIfNotNull(request.isRead(), notification::setIsRead);
        updateIfNotNull(request.readAt(), notification::setReadAt);
        notification.setInstance(instance);

        return mapper.toDTO(repository.save(notification));
    }

    @Override
    public void delete(Long id) {
        WorkflowNotification notification = repository.findById(id)
                .orElseThrow(() -> new WorkflowNotificationNotFound("Workflow notification not found with id:" + id));

        repository.delete(notification);
    }

    private <T> void updateIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
