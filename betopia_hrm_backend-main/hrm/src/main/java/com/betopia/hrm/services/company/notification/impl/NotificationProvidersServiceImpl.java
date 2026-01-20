package com.betopia.hrm.services.company.notification.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.NotificationProviders;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.repository.NotificationProvidersRepository;
import com.betopia.hrm.domain.company.request.NotificationProvidersRequest;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.services.company.notification.NotificationProvidersService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationProvidersServiceImpl implements NotificationProvidersService {

    private final NotificationProvidersRepository notificationProvidersRepository;

    private final CompanyRepository companyRepository;

    public NotificationProvidersServiceImpl(NotificationProvidersRepository notificationProvidersRepository
    ,CompanyRepository companyRepository) {
        this.notificationProvidersRepository = notificationProvidersRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public PaginationResponse<NotificationProviders> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<NotificationProviders> notificationProvidersPage = notificationProvidersRepository.findAll(pageable);

        List<NotificationProviders> notificationProviders = notificationProvidersPage.getContent();

        PaginationResponse<NotificationProviders> response = new PaginationResponse<>();

        response.setData(notificationProviders);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All notification providers fetch successful");

        Links links = Links.fromPage(notificationProvidersPage, "/notification-providers");
        response.setLinks(links);

        Meta meta = Meta.fromPage(notificationProvidersPage, "/notification-providers");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<NotificationProviders> getAllNotificationProviders() {
        return notificationProvidersRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

    }

    @Override
    public NotificationProviders getNotificationProvidersById(Long id) {
        return notificationProvidersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notifications Provider not found with id: " + id));
    }

    @Override
    public NotificationProviders store(NotificationProvidersRequest request) {
        NotificationProviders notificationProviders = new NotificationProviders();

        notificationProviders.setCompany (companyRepository.findById(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company not found with id: " + request.companyId())));
        notificationProviders.setChannel(request.channel());
        notificationProviders.setProviderKey(request.providerKey());
        notificationProviders.setStatus(request.status());
        return notificationProvidersRepository.save(notificationProviders);
    }

    @Override
    public NotificationProviders updateNotificationProviders(Long id, NotificationProvidersRequest request) {
        NotificationProviders notificationProviders = notificationProvidersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notifications Provider not found with id: " + id));

        notificationProviders.setCompany(request.companyId() != null ? companyRepository.findById(request.companyId()).orElse(null):
                notificationProviders.getCompany());

        notificationProviders.setChannel(request.channel() != null ? request.channel() : notificationProviders.getChannel());
        notificationProviders.setProviderKey(request.providerKey() != null ? request.providerKey() : notificationProviders.getProviderKey());
        notificationProviders.setStatus(request.status() != null ? request.status() : notificationProviders.getStatus());

        notificationProviders.setLastModifiedDate(LocalDateTime.now());

        return notificationProvidersRepository.save(notificationProviders);
    }

    @Override
    public void deleteNotificationProviders(Long id) {
        NotificationProviders notificationProviders = notificationProvidersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notifications Provider  not found with id: " + id));

        notificationProvidersRepository.delete(notificationProviders);
    }
}
