package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.QualificationRatingMethod;
import com.betopia.hrm.domain.employee.entity.QualificationRatingMethodDetails;
import com.betopia.hrm.domain.employee.exception.qualificationRatingMethod.QualificationRatingMethodNotFound;
import com.betopia.hrm.domain.employee.repository.QualificationRatingMethodRepository;
import com.betopia.hrm.domain.employee.request.DetailDto;
import com.betopia.hrm.domain.employee.request.MethodUpdateRequest;
import com.betopia.hrm.domain.employee.request.QualificationRatingMethodRequest;
import com.betopia.hrm.services.employee.QualificationRatingMethodService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class QualificationRatingMethodServiceImpl implements QualificationRatingMethodService {

    private QualificationRatingMethodRepository qualificationRatingMethodRepository;
    public QualificationRatingMethodServiceImpl(QualificationRatingMethodRepository qualificationRatingMethodRepository) {
        this.qualificationRatingMethodRepository = qualificationRatingMethodRepository;
    }

    @Override
    public PaginationResponse<QualificationRatingMethod> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<QualificationRatingMethod> qualificationRatingMethodPage = qualificationRatingMethodRepository.findAll(pageable);

        List<QualificationRatingMethod> qualificationRatingMethods = qualificationRatingMethodPage.getContent();

        PaginationResponse<QualificationRatingMethod> response = new PaginationResponse<>();

        response.setData(qualificationRatingMethods);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All qualificationRatingMethods fetch successful");

        Links links = Links.fromPage(qualificationRatingMethodPage, "/qualificationRatingMethods");
        response.setLinks(links);

        Meta meta = Meta.fromPage(qualificationRatingMethodPage, "/qualificationRatingMethods");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<QualificationRatingMethod> getAllQualificationRatingMethods() {

        return qualificationRatingMethodRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public QualificationRatingMethod insert(QualificationRatingMethodRequest request) {

        QualificationRatingMethod qualificationRatingMethod = new QualificationRatingMethod();
        qualificationRatingMethod.setCode(request.code());
        qualificationRatingMethod.setMethodName(request.methodName());

        request.qualificationRatingMethodDetails().stream().forEach(details->{
            QualificationRatingMethodDetails detail = new QualificationRatingMethodDetails();
            detail.setGrade(details.grade());
            detail.setAverageMark(details.averageMark());
            detail.setMaximumMark(details.maximumMark());
            detail.setMinimumMark(details.minimumMark());
            qualificationRatingMethod.addDetail(detail);
        });

        return qualificationRatingMethodRepository.save(qualificationRatingMethod);
    }

    @Override
    public QualificationRatingMethod show(Long qualificationRatingMethodRequestId) {

        QualificationRatingMethod qualificationRatingMethod = qualificationRatingMethodRepository.findById(qualificationRatingMethodRequestId)
                .orElseThrow(() -> new QualificationRatingMethodNotFound("QualificationRatingMethod not found with id: " + qualificationRatingMethodRequestId));

        return qualificationRatingMethod;
    }

    @Override
    public QualificationRatingMethod update(Long qualificationRatingMethodRequestId, MethodUpdateRequest request) {

        QualificationRatingMethod method = qualificationRatingMethodRepository.findById(qualificationRatingMethodRequestId)
                .orElseThrow(() -> new QualificationRatingMethodNotFound(
                        "QualificationRatingMethod not found with id: " + qualificationRatingMethodRequestId));

        method.setCode(request.code());
        method.setMethodName(request.methodName());

        // 2) index existing children by id
        Map<Long, QualificationRatingMethodDetails> existingById = method.getQualificationRatingMethodDetails()
                .stream()
                .filter(d -> d.getId() != null)
                .collect(Collectors.toMap(QualificationRatingMethodDetails::getId, Function.identity()));


        Set<Long> keepIds = new HashSet<>();
        if (request.qualificationRatingMethodDetails() != null) {
            for (DetailDto dto : request.qualificationRatingMethodDetails()) {
                if (dto.id() != null) {
                    QualificationRatingMethodDetails d = existingById.get(dto.id());
                    if (d == null) {
                        throw new IllegalArgumentException("Detail not found with id: " + dto.id());
                    }
                    d.setGrade(dto.grade());
                    d.setMaximumMark(dto.maximumMark());
                    d.setMinimumMark(dto.minimumMark());
                    d.setAverageMark(dto.averageMark());
                    keepIds.add(d.getId());
                } else {
                    QualificationRatingMethodDetails d = new QualificationRatingMethodDetails();
                    d.setGrade(dto.grade());
                    d.setMaximumMark(dto.maximumMark());
                    d.setMinimumMark(dto.minimumMark());
                    d.setAverageMark(dto.averageMark());
                    method.addDetail(d);
                }
            }

            method.getQualificationRatingMethodDetails()
                    .removeIf(d -> d.getId() != null && !keepIds.contains(d.getId()));
        }

        return qualificationRatingMethodRepository.save(method);
    }

    @Override
    public void delete(Long qualificationRatingMethodRequestId) {
        qualificationRatingMethodRepository.deleteById(qualificationRatingMethodRequestId);
    }
}
