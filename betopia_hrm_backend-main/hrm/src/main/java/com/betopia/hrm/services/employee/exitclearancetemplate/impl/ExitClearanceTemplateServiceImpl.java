package com.betopia.hrm.services.employee.exitclearancetemplate.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.ExitClearanceTemplateDTO;
import com.betopia.hrm.domain.dto.employee.mapper.ExitClearanceTemplateMapper;
import com.betopia.hrm.domain.employee.entity.ExitClearanceTemplate;
import com.betopia.hrm.domain.employee.repository.ExitClearanceTemplateRepository;
import com.betopia.hrm.domain.employee.request.ExitClearanceTemplateRequest;
import com.betopia.hrm.services.employee.exitclearancetemplate.ExitClearanceTemplateService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExitClearanceTemplateServiceImpl implements ExitClearanceTemplateService {

    private final ExitClearanceTemplateRepository exitClearanceTemplateRepository;
    private final ExitClearanceTemplateMapper exitClearanceTemplateMapper;

    public ExitClearanceTemplateServiceImpl(ExitClearanceTemplateRepository exitClearanceTemplateRepository,
                                            ExitClearanceTemplateMapper  exitClearanceTemplateMapper ) {
        this.exitClearanceTemplateRepository = exitClearanceTemplateRepository;
        this.exitClearanceTemplateMapper = exitClearanceTemplateMapper;
    }


    @Override
    public PaginationResponse<ExitClearanceTemplateDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<ExitClearanceTemplate> templatePage = exitClearanceTemplateRepository.findAll(pageable);

        // Get content from page
        List<ExitClearanceTemplate> exitClearanceTemplates = templatePage.getContent();

        // Convert entities to DTOs using MapStruct
        List<ExitClearanceTemplateDTO> templateDTOS = exitClearanceTemplateMapper.toDTOList(exitClearanceTemplates);

        // Create pagination response
        PaginationResponse<ExitClearanceTemplateDTO> response = new PaginationResponse<>();
        response.setData(templateDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All exit clearance successfully");

        // Set links
        Links links = Links.fromPage(templatePage, "/exit-template");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(templatePage, "/exit-template");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<ExitClearanceTemplateDTO> getAll() {
        List<ExitClearanceTemplate> exitClearanceTemplate = exitClearanceTemplateRepository.findAll();
        return exitClearanceTemplateMapper.toDTOList(exitClearanceTemplate);
    }

    @Override
    public ExitClearanceTemplateDTO store(ExitClearanceTemplateRequest request) {
        ExitClearanceTemplate exitClearanceTemplate = new ExitClearanceTemplate();


        exitClearanceTemplate.setTemplateName(request.templateName());
        exitClearanceTemplate.setDescription(request.description());
        exitClearanceTemplate.setIsDefault(request.isDefault());
        exitClearanceTemplate.setStatus(request.status());

        // Save entity
        ExitClearanceTemplate savedExitClearanceTemplate = exitClearanceTemplateRepository.save(exitClearanceTemplate);

        // Convert Entity to DTO and return
        return exitClearanceTemplateMapper.toDTO(savedExitClearanceTemplate);
    }

    @Override
    public ExitClearanceTemplateDTO show(Long id) {
        ExitClearanceTemplate exitClearanceTemplate = exitClearanceTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit clearance template not found with id: " + id));
        return exitClearanceTemplateMapper.toDTO(exitClearanceTemplate);
    }

    @Override
    public ExitClearanceTemplateDTO update(Long id, ExitClearanceTemplateRequest request) {
        ExitClearanceTemplate exitClearanceTemplate = exitClearanceTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit clearance template not found with id: " + id));


        exitClearanceTemplate.setTemplateName(request.templateName());
        exitClearanceTemplate.setDescription(request.description());
        exitClearanceTemplate.setIsDefault(request.isDefault());
        exitClearanceTemplate.setStatus(request.status());

        ExitClearanceTemplate savedExitClearanceTemplate = exitClearanceTemplateRepository.save(exitClearanceTemplate);

        return exitClearanceTemplateMapper.toDTO(savedExitClearanceTemplate);
    }

    @Override
    public void destroy(Long id) {
        ExitClearanceTemplate exitClearanceTemplate = exitClearanceTemplateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit clearance template not found"));

        exitClearanceTemplateRepository.delete(exitClearanceTemplate);
    }
}
