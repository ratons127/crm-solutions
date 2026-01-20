package com.betopia.hrm.services.company.notification.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.NotificationEvents;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.repository.NotificationEventsRepository;
import com.betopia.hrm.domain.company.request.NotificationEventsRequest;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.services.company.notification.NotificationEventsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationEventsServiceImpl implements NotificationEventsService {

    private final NotificationEventsRepository notificationEventsRepository;
    private final CompanyRepository companyRepository;

    public NotificationEventsServiceImpl(NotificationEventsRepository notificationEventsRepository
            , CompanyRepository companyRepository) {
        this.notificationEventsRepository = notificationEventsRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public PaginationResponse<NotificationEvents> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<NotificationEvents> notificationEventsPage = notificationEventsRepository.findAll(pageable);

        List<NotificationEvents> notificationEvents = notificationEventsPage.getContent();

        PaginationResponse<NotificationEvents> response = new PaginationResponse<>();

        response.setData(notificationEvents);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All notifications events fetch successful");

        Links links = Links.fromPage(notificationEventsPage, "/notification-events");
        response.setLinks(links);

        Meta meta = Meta.fromPage(notificationEventsPage, "/notification-events");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<NotificationEvents> getAllNotificationEvents() {
        return notificationEventsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public NotificationEvents getNotificationEventsById(Long id) {
        return notificationEventsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notifications Events not found with id: " + id));
    }

    @Override
    public NotificationEvents store(NotificationEventsRequest request) {
        NotificationEvents event = new NotificationEvents();

        event.setCompany (companyRepository.findById(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + request.companyId())));

        event.setEventKey(request.eventKey());
        event.setDisplayName(request.displayName());
        event.setDescription(request.description());
        event.setCategory(request.category());
        event.setIsSystem(request.isSystem());
        event.setStatus(request.status());

        return notificationEventsRepository.save(event);
    }

    @Override
    public NotificationEvents updateNotificationEvents(Long id, NotificationEventsRequest request) {
        NotificationEvents updateEvent = notificationEventsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notifications Events not found with id: " + id));

        updateEvent.setCompany(request.companyId() != null ? companyRepository.findById(request.companyId()).orElse(null):
                updateEvent.getCompany());

        updateEvent.setEventKey(request.eventKey() != null ? request.eventKey() : updateEvent.getEventKey());
        updateEvent.setDisplayName(request.displayName() != null ? request.displayName() : updateEvent.getDisplayName());
        updateEvent.setDescription(request.description() != null ? request.description() : updateEvent.getDescription());

        updateEvent.setCategory(request.category() != null ? request.category() : updateEvent.getCategory());
        updateEvent.setIsSystem(request.isSystem() != null ? request.isSystem() : updateEvent.getIsSystem());
        updateEvent.setStatus(request.status() != null ? request.status() : updateEvent.getStatus());

        updateEvent.setLastModifiedDate(LocalDateTime.now());

        return notificationEventsRepository.save(updateEvent);
    }

    @Override
    public void deleteNotificationEvents(Long id) {
        NotificationEvents deleteEvent = notificationEventsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notifications Events not found with id: " + id));

        notificationEventsRepository.delete(deleteEvent);
    }
}
