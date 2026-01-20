package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.FieldStudy;
import com.betopia.hrm.domain.employee.exception.fieldStudy.FieldStudyNotFoundException;
import com.betopia.hrm.domain.employee.repository.FieldStudyRepository;
import com.betopia.hrm.domain.employee.request.FieldStudyRequest;
import com.betopia.hrm.services.employee.FieldStudyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FieldStudyServiceImpl implements FieldStudyService {

    private final FieldStudyRepository fieldStudyRepository;

    public FieldStudyServiceImpl(FieldStudyRepository fieldStudyRepository) {
        this.fieldStudyRepository = fieldStudyRepository;
    }

    @Override
    public PaginationResponse<FieldStudy> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<FieldStudy> fieldStudyPage = fieldStudyRepository.findAll(pageable);

        List<FieldStudy> fieldStudies = fieldStudyPage.getContent();

        PaginationResponse<FieldStudy> response = new PaginationResponse<>();

        response.setData(fieldStudies);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All fieldStudy fetch successful");

        Links links = Links.fromPage(fieldStudyPage, "/fieldStudies");
        response.setLinks(links);

        Meta meta = Meta.fromPage(fieldStudyPage, "/fieldStudies");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<FieldStudy> getAllFieldStudy() {
        return fieldStudyRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public FieldStudy store(FieldStudyRequest request) {
        FieldStudy fieldStudy = new FieldStudy();
        fieldStudy.setQualificationLevelId(request.qualificationLevelId());
        fieldStudy.setFieldStudyName(request.fieldStudyName());
        fieldStudyRepository.save(fieldStudy);

        return fieldStudy;
    }

    @Override
    public FieldStudy show(Long fieldStudyId) {
        FieldStudy fieldStudy = fieldStudyRepository.findById(fieldStudyId)
                .orElseThrow(() -> new FieldStudyNotFoundException("FieldStudy not found with id: " + fieldStudyId));

        return fieldStudy;
    }

    @Override
    public FieldStudy update(Long fieldStudyId, FieldStudyRequest request) {
        FieldStudy fieldStudy = fieldStudyRepository.findById(fieldStudyId)
                .orElseThrow(() -> new FieldStudyNotFoundException("FieldStudy not found with id: " + fieldStudyId));

        fieldStudy.setQualificationLevelId(request.qualificationLevelId());
        fieldStudy.setFieldStudyName(request.fieldStudyName());
        fieldStudyRepository.save(fieldStudy);

        return fieldStudy;
    }

    @Override
    public void delete(Long fieldStudyId) {
        FieldStudy fieldStudy = fieldStudyRepository.findById(fieldStudyId)
                .orElseThrow(() -> new FieldStudyNotFoundException("FieldStudy not found with id: " + fieldStudyId));

        fieldStudyRepository.deleteById(fieldStudyId);
    }
}
