package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.QualificationLevel;
import com.betopia.hrm.domain.employee.exception.qualificationLevel.QualificationLevelNotFoundException;
import com.betopia.hrm.domain.employee.repository.QualificationLevelRepository;
import com.betopia.hrm.domain.employee.request.QualificationLevelRequest;
import com.betopia.hrm.services.employee.QualificationLevelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualificationLevelServiceImpl implements QualificationLevelService {

    private final QualificationLevelRepository qualificationLevelRepository;

    public QualificationLevelServiceImpl(QualificationLevelRepository qualificationLevelRepository) {
        this.qualificationLevelRepository = qualificationLevelRepository;
    }

    @Override
    public PaginationResponse<QualificationLevel> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<QualificationLevel> qualificationLevelPage = qualificationLevelRepository.findAll(pageable);

        List<QualificationLevel> qualificationLevels = qualificationLevelPage.getContent();

        PaginationResponse<QualificationLevel> response = new PaginationResponse<>();

        response.setData(qualificationLevels);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All qualificationLevels fetch successful");

        Links links = Links.fromPage(qualificationLevelPage, "/qualificationLevels");
        response.setLinks(links);

        Meta meta = Meta.fromPage(qualificationLevelPage, "/qualificationLevels");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<QualificationLevel> getAllQualificationLevel() {
        return qualificationLevelRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public QualificationLevel store(QualificationLevelRequest request) {
        QualificationLevel qualificationLevel = new QualificationLevel();
        qualificationLevel.setQualificationTypeId(request.qualificationTypeId());
        qualificationLevel.setLevelName(request.levelName());
        qualificationLevelRepository.save(qualificationLevel);

        return qualificationLevel;
    }

    @Override
    public QualificationLevel show(Long qualificationLevelId) {
        QualificationLevel qualificationLevel = qualificationLevelRepository.findById(qualificationLevelId)
                .orElseThrow(() -> new QualificationLevelNotFoundException("QualificationLevel not found with id: " + qualificationLevelId));

        return qualificationLevel;
    }

    @Override
    public QualificationLevel update(Long qualificationLevelId, QualificationLevelRequest request) {
        QualificationLevel qualificationLevel = qualificationLevelRepository.findById(qualificationLevelId)
                .orElseThrow(() -> new QualificationLevelNotFoundException("QualificationLevel not found with id: " + qualificationLevelId));

        qualificationLevel.setQualificationTypeId(request.qualificationTypeId());
        qualificationLevel.setLevelName(request.levelName());
        qualificationLevelRepository.save(qualificationLevel);

        return qualificationLevel;
    }

    @Override
    public void delete(Long qualificationLevelId) {
        QualificationLevel qualificationLevel = qualificationLevelRepository.findById(qualificationLevelId)
                .orElseThrow(() -> new QualificationLevelNotFoundException("QualificationLevel not found with id: " + qualificationLevelId));

        qualificationLevelRepository.deleteById(qualificationLevelId);
    }
}
