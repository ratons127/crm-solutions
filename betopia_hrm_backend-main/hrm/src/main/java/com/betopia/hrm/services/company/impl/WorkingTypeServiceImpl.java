package com.betopia.hrm.services.company.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.WorkingType;
import com.betopia.hrm.domain.company.repository.WorkingTypeRepository;
import com.betopia.hrm.domain.company.request.WorkingTypeRequest;
import com.betopia.hrm.services.company.WorkingTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class WorkingTypeServiceImpl implements WorkingTypeService {

    private final WorkingTypeRepository workingTypeRepository;

    public WorkingTypeServiceImpl(WorkingTypeRepository workingTypeRepository) {
        this.workingTypeRepository = workingTypeRepository;
    }

    @Override
    public PaginationResponse<WorkingType> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<WorkingType> workingTypePage = workingTypeRepository.findAll(pageable);

        List<WorkingType> workingTypes = workingTypePage.getContent();

        PaginationResponse<WorkingType> response = new PaginationResponse<>();

        response.setData(workingTypes);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All working type fetch successful");

        Links links = Links.fromPage(workingTypePage, "/workingTypes");
        response.setLinks(links);

        Meta meta = Meta.fromPage(workingTypePage, "/workingTypes");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<WorkingType> getAllWorkingType() {

        return workingTypeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public WorkingType insert(WorkingTypeRequest request) {

        WorkingType workingType = new WorkingType();
        workingType.setName(request.name());
        workingType.setStatus(request.status());
        workingTypeRepository.save(workingType);
        return workingType;
    }

    @Override
    public WorkingType show(Long workingTypeId) {
        WorkingType workingType = workingTypeRepository.findById(workingTypeId)
                .orElseThrow(() -> new RuntimeException("Working type not found with id: " + workingTypeId));

        return workingType;
    }

    @Override
    public WorkingType update(Long workingTypeId, WorkingTypeRequest request) {
        WorkingType workingType = workingTypeRepository.findById(workingTypeId)
                .orElseThrow(() -> new RuntimeException("Working type not found with id: " + workingTypeId));

        workingType.setName(request.name());
        workingType.setStatus(request.status());
        workingTypeRepository.save(workingType);
        return workingType;
    }

    @Override
    public void delete(Long workingTypeId) {
        workingTypeRepository.deleteById(workingTypeId);
    }
}
