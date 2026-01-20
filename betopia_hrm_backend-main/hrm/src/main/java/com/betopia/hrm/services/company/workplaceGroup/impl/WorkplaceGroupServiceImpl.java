package com.betopia.hrm.services.company.workplaceGroup.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.BusinessUnit;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.betopia.hrm.domain.company.exception.BusinessUnitNotFound;
import com.betopia.hrm.domain.company.exception.WorkplaceGroupNotFound;
import com.betopia.hrm.domain.company.repository.BusinessUnitRepository;
import com.betopia.hrm.domain.company.repository.WorkplaceGroupRepository;
import com.betopia.hrm.domain.company.request.WorkplaceGroupRequest;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.services.company.workplaceGroup.WorkplaceGroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkplaceGroupServiceImpl implements WorkplaceGroupService {

    private final WorkplaceGroupRepository workplaceGroupRepository;
    private final BusinessUnitRepository businessUnitRepository;


    public WorkplaceGroupServiceImpl(WorkplaceGroupRepository workplaceGroupRepository, BusinessUnitRepository businessUnitRepository, CompanyRepository companyRepository) {
        this.workplaceGroupRepository = workplaceGroupRepository;
        this.businessUnitRepository = businessUnitRepository;
    }

    @Override
    public PaginationResponse<WorkplaceGroup> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));
        Page<WorkplaceGroup> workplaceGroupPage = workplaceGroupRepository.findAll(pageable);
        List<WorkplaceGroup> workplaceGroups = workplaceGroupPage.getContent();

        PaginationResponse<WorkplaceGroup> response = new PaginationResponse<>();
        response.setData(workplaceGroups);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All workplace groups fetch successful");

        Links links = Links.fromPage(workplaceGroupPage, "/workplace-groups");
        response.setLinks(links);
        Meta meta = Meta.fromPage(workplaceGroupPage, "/workplace-group");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<WorkplaceGroup> getAllWorkplaceGroups() {
        return workplaceGroupRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public WorkplaceGroup store(WorkplaceGroupRequest request) {
        BusinessUnit businessUnit = businessUnitRepository.findById(request.businessUnitId())
                .orElseThrow(() -> new BusinessUnitNotFound("Business Unit not found"));

        WorkplaceGroup workplaceGroup = new WorkplaceGroup();

        workplaceGroup.setBusinessUnit(businessUnit);
        workplaceGroup.setName(request.name());
        workplaceGroup.setCode(request.code());
        workplaceGroup.setDescription(request.description());
        workplaceGroup.setStatus(request.status());

        workplaceGroupRepository.save(workplaceGroup);
        return workplaceGroup;
    }

    @Override
    public WorkplaceGroup show(Long workplaceGroupId) {
        WorkplaceGroup workplaceGroup = workplaceGroupRepository.findById(workplaceGroupId)
                .orElseThrow(() -> new WorkplaceGroupNotFound("Workplace group not found with id: " + workplaceGroupId));
        return workplaceGroup;
    }

    @Override
    public WorkplaceGroup update(Long workplaceGroupId, WorkplaceGroupRequest request) {
        WorkplaceGroup workplaceGroup = workplaceGroupRepository.findById(workplaceGroupId)
                .orElseThrow(() -> new WorkplaceGroupNotFound("Workplace Group not found with id: " + workplaceGroupId));

        BusinessUnit businessUnit = businessUnitRepository.findById(request.businessUnitId())
                .orElseThrow(() -> new BusinessUnitNotFound("Business Unit not found"));


        workplaceGroup.setBusinessUnit(businessUnit != null ? businessUnit : workplaceGroup.getBusinessUnit());
        workplaceGroup.setName(request.name() != null ? request.name() : workplaceGroup.getName());
        workplaceGroup.setCode(request.code() != null ? request.code() : workplaceGroup.getCode());
        workplaceGroup.setDescription(request.description() != null ? request.description() : workplaceGroup.getDescription());
        workplaceGroup.setStatus(request.status() != null ? request.status() : workplaceGroup.getStatus());

        workplaceGroupRepository.save(workplaceGroup);
        return workplaceGroup;
    }

    @Override
    public void destroy(Long workplaceGroupId) {
        workplaceGroupRepository.deleteById(workplaceGroupId);
    }
}
