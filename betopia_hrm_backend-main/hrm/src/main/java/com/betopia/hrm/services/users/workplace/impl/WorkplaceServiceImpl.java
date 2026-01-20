package com.betopia.hrm.services.users.workplace.impl;

import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.company.exception.WorkplaceGroupNotFound;
import com.betopia.hrm.domain.company.repository.WorkplaceGroupRepository;
import com.betopia.hrm.domain.users.entity.Workplace;
import com.betopia.hrm.domain.users.exception.workplace.WorkplaceNotFound;
import com.betopia.hrm.domain.users.repository.WorkplaceRepository;

import com.betopia.hrm.domain.users.request.WorkplaceRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.users.workplace.WorkplaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkplaceServiceImpl implements WorkplaceService {

    private final WorkplaceRepository workplaceRepository;

    private final WorkplaceGroupRepository workplaceGroupRepository;

    public WorkplaceServiceImpl(WorkplaceRepository workplaceRepository, WorkplaceGroupRepository workplaceGroupRepository) {
        this.workplaceRepository = workplaceRepository;
        this.workplaceGroupRepository = workplaceGroupRepository;
    }

    @Override
    public PaginationResponse<Workplace> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<Workplace> workplacePage = workplaceRepository.findAll(pageable);

        List<Workplace> workplaces = workplacePage.getContent();

        PaginationResponse<Workplace> response = new PaginationResponse<>();

        response.setData(workplaces);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All workplaces fetch successful");

        Links links = Links.fromPage(workplacePage, "/workplaces");
        response.setLinks(links);

        Meta meta = Meta.fromPage(workplacePage, "/workplace");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<Workplace> getAllWorkplaces() {

        return workplaceRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Workplace store(WorkplaceRequest request) {
        Workplace workplace = new Workplace();

        WorkplaceGroup workplaceGroup = workplaceGroupRepository.findById(request.workplaceGroupId())
                .orElseThrow(() -> new WorkplaceGroupNotFound("Workplace group not found"));



        workplace.setWorkplaceGroup(workplaceGroup);
        workplace.setName(request.name());
        workplace.setCode(request.code());
        workplace.setDescription(request.description());
        workplace.setAddress(request.address());
        workplace.setStatus(request.status());

        workplaceRepository.save(workplace);

        return workplace;
    }

    @Override
    public Workplace show(Long workplaceId) {
        Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(() -> new WorkplaceNotFound("Workplace not found with id: " + workplaceId));

        return workplace;
    }

    @Override
    public Workplace update(Long workplaceId, WorkplaceRequest request) {
    Workplace workplace = workplaceRepository.findById(workplaceId)
                .orElseThrow(() -> new WorkplaceNotFound("workplace not found with id: " + workplaceId));

        WorkplaceGroup workplaceGroup = workplaceGroupRepository.findById(request.workplaceGroupId())
                .orElseThrow(() -> new WorkplaceGroupNotFound("workplace group not found"));

        workplace.setWorkplaceGroup(workplaceGroup != null ? workplaceGroup : workplace.getWorkplaceGroup());
        workplace.setName(request.name() != null ? request.name() : workplace.getName());
        workplace.setCode(request.code() != null ? request.code() : workplace.getCode());
        workplace.setDescription(request.description() != null ? request.description() : workplace.getDescription());
        workplace.setAddress(request.address() != null ? request.address() : workplace.getAddress());
        workplace.setStatus(request.status() != null ? request.status() : workplace.getStatus());

        workplaceRepository.save(workplace);

        return workplace;
    }

    @Override
    public void destroy(Long workplaceId) {
        workplaceRepository.deleteById(workplaceId);
    }
}
