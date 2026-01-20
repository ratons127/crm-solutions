package com.betopia.hrm.services.employee.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.Grade;
import com.betopia.hrm.domain.employee.exception.grade.GradeNotFoundException;
import com.betopia.hrm.domain.employee.repository.GradeRepository;
import com.betopia.hrm.domain.employee.request.GradeRequest;
import com.betopia.hrm.services.employee.GradeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;

    public GradeServiceImpl(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    @Override
    public PaginationResponse<Grade> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page -1, perPage, Sort.by(direction, "id"));

        Page<Grade> gradePage = gradeRepository.findAll(pageable);

        List<Grade> grades = gradePage.getContent();

        PaginationResponse<Grade> response = new PaginationResponse<>();

        response.setData(grades);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All grade fetch successful");

        Links links = Links.fromPage(gradePage, "/grades");
        response.setLinks(links);

        Meta meta = Meta.fromPage(gradePage, "/grades");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<Grade> getAllGrades() {

        return gradeRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public Grade insert(GradeRequest request) {

        Grade grade = new Grade();
        grade.setName(request.name());
        grade.setCode(request.code());
        grade.setDescription(request.description());
        grade.setStatus(request.status());
        gradeRepository.save(grade);

        return grade;
    }

    @Override
    public Grade show(Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new GradeNotFoundException("Grade not found with id: " + gradeId));

        return grade;
    }

    @Override
    public Grade update(Long gradeId, GradeRequest request) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new GradeNotFoundException("Grade not found with id: " + gradeId));

        grade.setName(request.name());
        grade.setCode(request.code());
        grade.setDescription(request.description());
        grade.setStatus(request.status());
        gradeRepository.save(grade);
        return grade;
    }

    @Override
    public void delete(Long gradeId) {
        Grade grade = gradeRepository.findById(gradeId)
                .orElseThrow(() -> new GradeNotFoundException("Grade not found with id: " + gradeId));

        gradeRepository.deleteById(gradeId);
    }
}
