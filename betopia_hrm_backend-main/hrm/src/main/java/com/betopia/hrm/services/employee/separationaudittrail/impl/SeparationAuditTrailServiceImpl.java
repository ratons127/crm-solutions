package com.betopia.hrm.services.employee.separationaudittrail.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.SeparationAuditTrailDTO;
import com.betopia.hrm.domain.dto.employee.mapper.SeparationAuditTrailMapper;
import com.betopia.hrm.domain.employee.entity.EmployeeSeparations;
import com.betopia.hrm.domain.employee.entity.SeparationAuditTrail;
import com.betopia.hrm.domain.employee.repository.EmployeeSeparationsRepository;
import com.betopia.hrm.domain.employee.repository.SeparationAuditTrailRepository;
import com.betopia.hrm.domain.employee.request.SeparationAuditTrailRequest;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.separationaudittrail.SeparationAuditTrailService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeparationAuditTrailServiceImpl implements SeparationAuditTrailService {

    private final SeparationAuditTrailRepository separationAuditTrailRepository;
    private final EmployeeSeparationsRepository employeeSeparationsRepository;
    private final UserRepository userRepository;
    private final SeparationAuditTrailMapper separationAuditTrailMapper;

    public SeparationAuditTrailServiceImpl(SeparationAuditTrailRepository separationAuditTrailRepository,
                                           EmployeeSeparationsRepository employeeSeparationsRepository,
                                           UserRepository userRepository, SeparationAuditTrailMapper separationAuditTrailMapper) {
        this.separationAuditTrailRepository = separationAuditTrailRepository;
        this.employeeSeparationsRepository = employeeSeparationsRepository;
        this.userRepository = userRepository;
        this.separationAuditTrailMapper = separationAuditTrailMapper;
    }

    @Override
    public PaginationResponse<SeparationAuditTrailDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<SeparationAuditTrail> trailPage = separationAuditTrailRepository.findAll(pageable);

        // Get content from page
        List<SeparationAuditTrail>  separationAuditTrails = trailPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<SeparationAuditTrailDTO> separationAuditTrailsDTOS = separationAuditTrailMapper.toDTOList(separationAuditTrails);

        // Create pagination response
        PaginationResponse<SeparationAuditTrailDTO> response = new PaginationResponse<>();
        response.setData(separationAuditTrailsDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All separation audit successfully");

        // Set links
        Links links = Links.fromPage(trailPage, "/separation-audit");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(trailPage, "/separation-audit");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<SeparationAuditTrailDTO> getAll() {
        List<SeparationAuditTrail> separationAuditTrails = separationAuditTrailRepository.findAll();
        return separationAuditTrailMapper.toDTOList(separationAuditTrails);
    }

    @Override
    public SeparationAuditTrailDTO store(SeparationAuditTrailRequest request) {
        SeparationAuditTrail separationAuditTrail = new SeparationAuditTrail();

        EmployeeSeparations employeeSeparations = employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found with id: " + request.separationId()));

        User approver = userRepository.findById(request.performedById())
                .orElseThrow(() -> new UsernameNotFoundException("Performer not found with id: " + request.performedById()));

        separationAuditTrail.setSeparation(employeeSeparations);
        separationAuditTrail.setAction(request.action());
        separationAuditTrail.setActionDetails(request.actionDetails());
        separationAuditTrail.setOldValues(request.oldValues());
        separationAuditTrail.setNewValues(request.newValues());
        separationAuditTrail.setIpAddress(request.ipAddress());
        separationAuditTrail.setUserAgent(request.userAgent());
        separationAuditTrail.setPerformedBy(approver);
        // Save entity
        SeparationAuditTrail saved = separationAuditTrailRepository.save(separationAuditTrail);

        // Convert entity to DTO
        return separationAuditTrailMapper.toDTO(saved);
    }

    @Override
    public SeparationAuditTrailDTO show(Long id) {
        SeparationAuditTrail separationAuditTrail = separationAuditTrailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("separation audit trail not found with id: " + id));
        return separationAuditTrailMapper.toDTO(separationAuditTrail);
    }

    @Override
    public SeparationAuditTrailDTO update(Long id, SeparationAuditTrailRequest request) {
        SeparationAuditTrail separationAuditTrail = separationAuditTrailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("separation audit trail template not found with id: " + id));


        EmployeeSeparations employeeSeparations = employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found with id: " + request.separationId()));

        User approver = userRepository.findById(request.performedById())
                .orElseThrow(() -> new UsernameNotFoundException("Performer not found with id: " + request.performedById()));

        separationAuditTrail.setSeparation(employeeSeparations);
        separationAuditTrail.setAction(request.action());
        separationAuditTrail.setActionDetails(request.actionDetails());
        separationAuditTrail.setOldValues(request.oldValues());
        separationAuditTrail.setNewValues(request.newValues());
        // Save entity
        SeparationAuditTrail saved = separationAuditTrailRepository.save(separationAuditTrail);

        // Convert entity to DTO
        return separationAuditTrailMapper.toDTO(saved);

    }

    @Override
    public void destroy(Long id) {
        SeparationAuditTrail separationAuditTrail = separationAuditTrailRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit interview template not found"));

        separationAuditTrailRepository.delete(separationAuditTrail);
    }
}
