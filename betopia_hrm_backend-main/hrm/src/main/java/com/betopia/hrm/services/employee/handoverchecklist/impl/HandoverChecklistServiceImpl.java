package com.betopia.hrm.services.employee.handoverchecklist.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.HandoverChecklistDTO;
import com.betopia.hrm.domain.dto.employee.SeparationAuditTrailDTO;
import com.betopia.hrm.domain.dto.employee.mapper.HandoverChecklistMapper;
import com.betopia.hrm.domain.employee.entity.HandoverChecklist;
import com.betopia.hrm.domain.employee.entity.SeparationAuditTrail;
import com.betopia.hrm.domain.employee.entity.SeparationDocuments;
import com.betopia.hrm.domain.employee.entity.SeparationReasons;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeSeparationsRepository;
import com.betopia.hrm.domain.employee.repository.HandoverChecklistRepository;
import com.betopia.hrm.domain.employee.request.HandoverChecklistRequest;
import com.betopia.hrm.services.employee.handoverchecklist.HandoverChecklistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class HandoverChecklistServiceImpl implements HandoverChecklistService {

    private final HandoverChecklistRepository handoverChecklistRepository;
    private final EmployeeSeparationsRepository employeeSeparationsRepository;
    private final HandoverChecklistMapper handoverChecklistMapper;
    private final EmployeeRepository employeeRepository;

    public HandoverChecklistServiceImpl(HandoverChecklistRepository handoverChecklistRepository,
                                        EmployeeSeparationsRepository employeeSeparationsRepository,
                                        HandoverChecklistMapper handoverChecklistMapper, EmployeeRepository employeeRepository){
        this.handoverChecklistRepository = handoverChecklistRepository;
        this.employeeSeparationsRepository = employeeSeparationsRepository;
        this.handoverChecklistMapper = handoverChecklistMapper;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public PaginationResponse<HandoverChecklistDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<HandoverChecklist> trailPage = handoverChecklistRepository.findAll(pageable);

        // Get content from page
        List<HandoverChecklist>  separationAuditTrails = trailPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<HandoverChecklistDTO> separationAuditTrailsDTOS = handoverChecklistMapper.toDTOList(separationAuditTrails);

        // Create pagination response
        PaginationResponse<HandoverChecklistDTO> response = new PaginationResponse<>();
        response.setData(separationAuditTrailsDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All handover checklist successfully");

        // Set links
        Links links = Links.fromPage(trailPage, "/handover");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(trailPage, "/handover");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<HandoverChecklistDTO> getAll() {
        List<HandoverChecklist> handoverChecklists = handoverChecklistRepository.findAll();
        return handoverChecklistMapper.toDTOList(handoverChecklists);
    }

    @Override
    public HandoverChecklistDTO show(Long id) {
        HandoverChecklist HandoverChecklist = handoverChecklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("separation audit trail not found with id: " + id));
        return handoverChecklistMapper.toDTO(HandoverChecklist);
    }

    @Override
    public HandoverChecklistDTO update(Long id, HandoverChecklistRequest request) {
        return null;
    }


    @Override
    public void destroy(Long id) {
        HandoverChecklist documents = handoverChecklistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Separation documents not not found"));

        handoverChecklistRepository.delete(documents);
    }

    @Override
    public HandoverChecklistDTO store(HandoverChecklistRequest request) {
        HandoverChecklist separationReasons = new HandoverChecklist();

        separationReasons.setSeparation(employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found")));

        separationReasons.setHandoverTo(employeeRepository.findById(request.handoverToId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found")));

        separationReasons.setItemDescription(request.itemDescription());
        separationReasons.setStatus(request.status());
        separationReasons.setCompletedDate(request.completedDate());
        separationReasons.setRemarks(request.remarks());
        separationReasons.setEvidencePath(request.evidencePath());
        // Save entity
        HandoverChecklist saved = handoverChecklistRepository.save(separationReasons);

        // Convert entity to DTO
        return handoverChecklistMapper.toDTO(saved);
    }

}
