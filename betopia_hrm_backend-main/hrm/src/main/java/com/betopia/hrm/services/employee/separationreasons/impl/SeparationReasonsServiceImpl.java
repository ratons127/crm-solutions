package com.betopia.hrm.services.employee.separationreasons.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.SeparationReasonsDTO;
import com.betopia.hrm.domain.dto.employee.mapper.SeparationReasonsMapper;
import com.betopia.hrm.domain.employee.entity.SeparationReasons;
import com.betopia.hrm.domain.employee.repository.SeparationReasonsRepository;
import com.betopia.hrm.domain.employee.request.SeparationReasonsRequest;
import com.betopia.hrm.services.employee.separationreasons.SeparationReasonsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class SeparationReasonsServiceImpl implements SeparationReasonsService {

    private final SeparationReasonsRepository separationReasonsRepository;
    private final SeparationReasonsMapper separationReasonsMapper;

    public SeparationReasonsServiceImpl(SeparationReasonsRepository separationReasonsRepository,
                                        SeparationReasonsMapper separationReasonsMapper) {
        this.separationReasonsRepository = separationReasonsRepository;
        this.separationReasonsMapper = separationReasonsMapper;
    }

    @Override
    public PaginationResponse<SeparationReasonsDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<SeparationReasons> reasonsPage = separationReasonsRepository.findAll(pageable);

        // Get content from page
        List<SeparationReasons> separationReasons = reasonsPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<SeparationReasonsDTO> separationReasonsDTOS = separationReasonsMapper.toDTOList(separationReasons);

        // Create pagination response
        PaginationResponse<SeparationReasonsDTO> response = new PaginationResponse<>();
        response.setData(separationReasonsDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All separation reasons successfully");

        // Set links
        Links links = Links.fromPage(reasonsPage, "/seperation-reason");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(reasonsPage, "/seperation-reason");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<SeparationReasonsDTO> getAll() {
        List<SeparationReasons> separationReasons = separationReasonsRepository.findAll();
        return separationReasonsMapper.toDTOList(separationReasons);
    }

    @Override
    public SeparationReasonsDTO show(Long id) {
        SeparationReasons separationReasons = separationReasonsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("separation reason not found with id: " + id));
        return separationReasonsMapper.toDTO(separationReasons);
    }

    @Override
    public SeparationReasonsDTO update(Long id, SeparationReasonsRequest request) {
        SeparationReasons separationReasons = separationReasonsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("separation reason not found with id: " + id));

        separationReasons.setSeparationType(request.separationType());
        separationReasons.setReasonCode(request.reasonCode());
        separationReasons.setReasonText(request.reasonText());
        separationReasons.setStatus(request.status());
        separationReasons.setDisplayOrder(request.displayOrder());
        // Save entity
        SeparationReasons saved = separationReasonsRepository.save(separationReasons);

        // Convert entity to DTO
        return separationReasonsMapper.toDTO(saved);
    }

    @Override
    public void destroy(Long id) {
        SeparationReasons separationReasons = separationReasonsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("separation reason not found"));

        separationReasonsRepository.delete(separationReasons);
    }

    @Override
    public SeparationReasonsDTO store(SeparationReasonsRequest request) {
        SeparationReasons separationReasons = new SeparationReasons();

        separationReasons.setSeparationType(request.separationType());
        separationReasons.setReasonCode(request.reasonCode());
        separationReasons.setReasonText(request.reasonText());
        separationReasons.setStatus(request.status());
        separationReasons.setDisplayOrder(request.displayOrder());
        // Save entity
        SeparationReasons saved = separationReasonsRepository.save(separationReasons);

        // Convert entity to DTO
        return separationReasonsMapper.toDTO(saved);
    }


}
