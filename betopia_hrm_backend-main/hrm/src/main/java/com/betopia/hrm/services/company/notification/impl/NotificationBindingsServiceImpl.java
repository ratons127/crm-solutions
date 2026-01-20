package com.betopia.hrm.services.company.notification.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.NotificationBindings;
import com.betopia.hrm.domain.company.entity.NotificationEvents;
import com.betopia.hrm.domain.company.repository.NotificationBindingsRepository;
import com.betopia.hrm.domain.company.repository.NotificationEventsRepository;
import com.betopia.hrm.domain.company.request.NotificationBindingsRequest;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.services.company.notification.NotificationBindingsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationBindingsServiceImpl implements NotificationBindingsService {

    private final NotificationBindingsRepository notificationBindingsRepository;
    private final NotificationEventsRepository notificationEventsRepository;
    private final CompanyRepository companyRepository;

    public NotificationBindingsServiceImpl(NotificationBindingsRepository notificationBindingsRepository,
                                           NotificationEventsRepository notificationEventsRepository, CompanyRepository companyRepository) {
        this.notificationBindingsRepository = notificationBindingsRepository;
        this.notificationEventsRepository = notificationEventsRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public PaginationResponse<NotificationBindings> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<NotificationBindings> notificationBindingsPage = notificationBindingsRepository.findAll(pageable);

        List<NotificationBindings> notificationBindings = notificationBindingsPage.getContent();

        PaginationResponse<NotificationBindings> response = new PaginationResponse<>();

        response.setData(notificationBindings);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All notifications bindings fetch successful");

        Links links = Links.fromPage(notificationBindingsPage, "/notification-bindings");
        response.setLinks(links);

        Meta meta = Meta.fromPage(notificationBindingsPage, "/notification-bindings");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<NotificationBindings> getAllNotificationBindings() {
        return notificationBindingsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public NotificationBindings getNotificationBindingsById(Long id) {
        return notificationBindingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notifications bindings not found with id: " + id));
    }

    @Override
    public NotificationBindings store(NotificationBindingsRequest request) {
        NotificationBindings binding = new NotificationBindings();

        binding.setCompany(companyRepository.findById(request.companyId())
         .orElseThrow(() -> new RuntimeException("Company not found with id: " + request.companyId()))
        );

        binding.setEvent(notificationEventsRepository.findById(request.eventId())
                .orElseThrow(() -> new RuntimeException("Notification bindings not found with id: " + request.eventId()))
        );

        binding.setAudienceType(request.audienceType());
        binding.setAudienceRefId(request.audienceRefId());
        binding.setPriority(request.priority());
        binding.setStatus(request.status());

        return notificationBindingsRepository.save(binding);
    }

    @Override
    public NotificationBindings updateNotificationBindings(Long id, NotificationBindingsRequest request) {
        NotificationBindings updateBinding = notificationBindingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notifications bindings not found with id: " + id));

        updateBinding.setCompany(request.companyId() != null ? companyRepository.findById(request.companyId()).orElse(null):
                updateBinding.getCompany());

        updateBinding.setAudienceType(request.audienceType() != null ? request.audienceType() : updateBinding.getAudienceType());
        updateBinding.setAudienceRefId(request.audienceRefId() != null ? request.audienceRefId() : updateBinding.getAudienceRefId());
        updateBinding.setPriority(request.priority() != null ? request.priority() : updateBinding.getPriority());
        updateBinding.setStatus(request.status() != null ? request.status() : updateBinding.getStatus());
        updateBinding.setLastModifiedDate(LocalDateTime.now());

        return notificationBindingsRepository.save(updateBinding);
    }

    @Override
    public void deleteNotificationBindings(Long id) {
        NotificationBindings deleteBinding = notificationBindingsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notifications bindings not found with id: " + id));

        notificationBindingsRepository.delete(deleteBinding);
    }
}
