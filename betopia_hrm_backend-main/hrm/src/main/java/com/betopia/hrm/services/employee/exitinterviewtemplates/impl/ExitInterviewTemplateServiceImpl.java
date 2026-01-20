package com.betopia.hrm.services.employee.exitinterviewtemplates.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.ExitInterviewTemplatesDTO;
import com.betopia.hrm.domain.dto.employee.mapper.ExitInterviewTemplatesMapper;
import com.betopia.hrm.domain.employee.entity.ExitInterviewTemplates;
import com.betopia.hrm.domain.employee.repository.ExitInterviewTemplatesRepository;
import com.betopia.hrm.domain.employee.request.ExitInterviewTemplatesRequest;
import com.betopia.hrm.services.employee.exitinterviewtemplates.ExitInterviewTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExitInterviewTemplateServiceImpl implements ExitInterviewTemplateService {

    private final ExitInterviewTemplatesRepository exitInterviewTemplatesRepository;
    private final ExitInterviewTemplatesMapper exitInterviewTemplatesMapper;

    public ExitInterviewTemplateServiceImpl(ExitInterviewTemplatesRepository exitInterviewTemplatesRepository,
                                            ExitInterviewTemplatesMapper exitInterviewTemplatesMapper) {
        this.exitInterviewTemplatesRepository = exitInterviewTemplatesRepository;
        this.exitInterviewTemplatesMapper = exitInterviewTemplatesMapper;
    }


    @Override
    public PaginationResponse<ExitInterviewTemplatesDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<ExitInterviewTemplates> interviewPage = exitInterviewTemplatesRepository.findAll(pageable);

        // Get content from page
        List<ExitInterviewTemplates>  exitInterviews = interviewPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<ExitInterviewTemplatesDTO> exitInterviewsDTOS = exitInterviewTemplatesMapper.toDTOList(exitInterviews);

        // Create pagination response
        PaginationResponse<ExitInterviewTemplatesDTO> response = new PaginationResponse<>();
        response.setData(exitInterviewsDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All exit interview template successfully");

        // Set links
        Links links = Links.fromPage(interviewPage, "/exit-interview-template");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(interviewPage, "/exit-interview-template");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<ExitInterviewTemplatesDTO> getAll() {
        List<ExitInterviewTemplates> exitInterviewTemplates = exitInterviewTemplatesRepository.findAll();
        return exitInterviewTemplatesMapper.toDTOList(exitInterviewTemplates);
    }

    @Override
    public ExitInterviewTemplatesDTO store(ExitInterviewTemplatesRequest request) {
        ExitInterviewTemplates exitInterview = new ExitInterviewTemplates();

        exitInterview.setSeparationType(request.separationType());

        exitInterview.setTemplateName(request.templateName());
        exitInterview.setQuestions(request.questions());
        exitInterview.setIsDefault(request.isDefault());
        exitInterview.setStatus(request.status());
        // Save entity
        ExitInterviewTemplates saved = exitInterviewTemplatesRepository.save(exitInterview);
        System.err.println("template saved");

        // Convert entity to DTO
        return exitInterviewTemplatesMapper.toDTO(saved);
    }

    @Override
    public ExitInterviewTemplatesDTO show(Long id) {
        ExitInterviewTemplates exitInterview = exitInterviewTemplatesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit interview template not found with id: " + id));
        return exitInterviewTemplatesMapper.toDTO(exitInterview);
    }

    @Override
    public ExitInterviewTemplatesDTO update(Long id, ExitInterviewTemplatesRequest request) {
        ExitInterviewTemplates exitInterview = exitInterviewTemplatesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exit Interview template not found with id: " + id));

        exitInterview.setSeparationType(request.separationType());

        exitInterview.setTemplateName(request.templateName());
        exitInterview.setQuestions(request.questions());
        exitInterview.setIsDefault(request.isDefault());
        exitInterview.setStatus(request.status());

        // Save entity
        ExitInterviewTemplates saved = exitInterviewTemplatesRepository.save(exitInterview);

        // Convert entity to DTO
        return exitInterviewTemplatesMapper.toDTO(saved);
    }

    @Override
    public void destroy(Long id) {
        ExitInterviewTemplates exitInterview = exitInterviewTemplatesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit interview template not found"));

        exitInterviewTemplatesRepository.delete(exitInterview);
    }
}
