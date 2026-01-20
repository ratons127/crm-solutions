package com.betopia.hrm.services.company.notification;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.NotificationEvents;
import com.betopia.hrm.domain.company.request.NotificationEventsRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NotificationEventsService {

    PaginationResponse<NotificationEvents> index(Sort.Direction direction, int page, int perPage);

    List<NotificationEvents> getAllNotificationEvents();

    NotificationEvents getNotificationEventsById(Long id);

    NotificationEvents store(NotificationEventsRequest request);

    NotificationEvents updateNotificationEvents(Long id, NotificationEventsRequest request);

    void deleteNotificationEvents(Long id);
}
