package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.QualificationType;
import com.betopia.hrm.domain.employee.exception.qualificationType.QualificationTypeNotFoundException;
import com.betopia.hrm.domain.employee.repository.QualificationTypeRepository;
import com.betopia.hrm.domain.employee.request.QualificationTypeRequest;
import com.betopia.hrm.services.employee.QualificationTypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualificationTypeServiceImpl implements QualificationTypeService {

    private final QualificationTypeRepository qualificationTypeRepository;

    public QualificationTypeServiceImpl(QualificationTypeRepository qualificationTypeRepository) {
        this.qualificationTypeRepository = qualificationTypeRepository;
    }

    @Override
    public PaginationResponse<QualificationType> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<QualificationType> qualificationTypePage = qualificationTypeRepository.findAll(pageable);

        List<QualificationType> qualificationTypes = qualificationTypePage.getContent();

        PaginationResponse<QualificationType> response = new PaginationResponse<>();

        response.setData(qualificationTypes);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All qualificationTypes fetch successful");

        Links links = Links.fromPage(qualificationTypePage, "/qualificationTypes");
        response.setLinks(links);

        Meta meta = Meta.fromPage(qualificationTypePage, "/qualificationTypes");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<QualificationType> getAllQualificationType() {
        return qualificationTypeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public QualificationType store(QualificationTypeRequest request) {
        QualificationType qualificationType = new QualificationType();
        qualificationType.setTypeName(request.typeName());

        qualificationTypeRepository.save(qualificationType);

        return qualificationType;
    }

    @Override
    public QualificationType show(Long qualificationTypeId) {
        QualificationType qualificationType = qualificationTypeRepository.findById(qualificationTypeId)
                .orElseThrow(() -> new QualificationTypeNotFoundException("QualificationType not found with id: " + qualificationTypeId));

        return qualificationType;
    }

    @Override
    public QualificationType update(Long qualificationTypeId, QualificationTypeRequest request) {
        QualificationType qualificationType = qualificationTypeRepository.findById(qualificationTypeId)
                .orElseThrow(() -> new QualificationTypeNotFoundException("QualificationType not found with id: " + qualificationTypeId));

        qualificationType.setTypeName(request.typeName());

        qualificationTypeRepository.save(qualificationType);

        return qualificationType;
    }

    @Override
    public void delete(Long qualificationTypeId) {
        QualificationType qualificationType = qualificationTypeRepository.findById(qualificationTypeId)
                .orElseThrow(() -> new QualificationTypeNotFoundException("QualificationType not found with id: " + qualificationTypeId));

        qualificationTypeRepository.deleteById(qualificationTypeId);
    }
}
