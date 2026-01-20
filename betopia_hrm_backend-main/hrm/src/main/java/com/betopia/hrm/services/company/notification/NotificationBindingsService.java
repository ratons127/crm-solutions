package com.betopia.hrm.services.company.notification;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.NotificationBindings;
import com.betopia.hrm.domain.company.request.NotificationBindingsRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NotificationBindingsService {

    PaginationResponse<NotificationBindings> index(Sort.Direction direction, int page, int perPage);

    List<NotificationBindings> getAllNotificationBindings();

    NotificationBindings getNotificationBindingsById(Long id);

    NotificationBindings store(NotificationBindingsRequest request);

    NotificationBindings updateNotificationBindings(Long id, NotificationBindingsRequest request);

    void deleteNotificationBindings(Long id);
}
