package com.betopia.hrm.services.company.notification;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.NotificationProviders;
import com.betopia.hrm.domain.company.request.NotificationProvidersRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface NotificationProvidersService {

    PaginationResponse<NotificationProviders> index(Sort.Direction direction, int page, int perPage);

    List<NotificationProviders> getAllNotificationProviders();

    NotificationProviders getNotificationProvidersById(Long id);

    NotificationProviders store(NotificationProvidersRequest request);

    NotificationProviders updateNotificationProviders(Long id, NotificationProvidersRequest request);

    void deleteNotificationProviders(Long id);
}
