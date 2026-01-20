package com.betopia.hrm.services.employee.accessrevocationlog.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.AccessRevocationLogDTO;
import com.betopia.hrm.domain.dto.employee.mapper.AccessRevocationLogMapper;
import com.betopia.hrm.domain.employee.entity.AccessRevocationLog;
import com.betopia.hrm.domain.employee.repository.AccessRevocationLogRepository;
import com.betopia.hrm.domain.employee.repository.EmployeeSeparationsRepository;
import com.betopia.hrm.domain.employee.request.AccessRevocationLogRequest;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.employee.accessrevocationlog.AccessRevocationLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccessRevocationLogServiceImpl implements AccessRevocationLogService {

    private final AccessRevocationLogRepository accessRevocationLogRepository;
    private final EmployeeSeparationsRepository employeeSeparationsRepository;
    private final UserRepository userRepository;
    private final AccessRevocationLogMapper accessRevocationLogMapper;

    public AccessRevocationLogServiceImpl(AccessRevocationLogRepository accessRevocationLogRepository,
                                          EmployeeSeparationsRepository employeeSeparationsRepository,
                                          UserRepository userRepository, AccessRevocationLogMapper accessRevocationLogMapper) {
        this.accessRevocationLogRepository = accessRevocationLogRepository;
        this.employeeSeparationsRepository = employeeSeparationsRepository;
        this.userRepository = userRepository;
        this.accessRevocationLogMapper = accessRevocationLogMapper;
    }

    @Override
    public PaginationResponse<AccessRevocationLogDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<AccessRevocationLog> LogPage = accessRevocationLogRepository.findAll(pageable);

        // Get content from page
        List<AccessRevocationLog>  accessRevocationLogs = LogPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<AccessRevocationLogDTO> accessRevocationLogDTOS = accessRevocationLogMapper.toDTOList(accessRevocationLogs);

        // Create pagination response
        PaginationResponse<AccessRevocationLogDTO> response = new PaginationResponse<>();
        response.setData(accessRevocationLogDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All access revocation log successfully");

        // Set links
        Links links = Links.fromPage(LogPage, "/access-log");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(LogPage, "/access-log");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<AccessRevocationLogDTO> getAll() {
        List<AccessRevocationLog> accessRevocationLogs = accessRevocationLogRepository.findAll();
        return accessRevocationLogMapper.toDTOList(accessRevocationLogs);
    }

    @Override
    public AccessRevocationLogDTO store(AccessRevocationLogRequest request) {
        AccessRevocationLog accessRevocationLogs = new AccessRevocationLog();

        accessRevocationLogs.setSeparation(employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found")));

        accessRevocationLogs.setRevokedBy(userRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Revoked by not found")));

        accessRevocationLogs.setSystemName(request.systemName());
        accessRevocationLogs.setAccessType(request.accessType());
        accessRevocationLogs.setRevocationDate(request.revocationDate());
        accessRevocationLogs.setAccessStatus(request.accessStatus());
        accessRevocationLogs.setErrorMessage(request.errorMessage());

        accessRevocationLogs.setRetryCount(request.retryCount());


        // Save entity
        AccessRevocationLog save = accessRevocationLogRepository.save(accessRevocationLogs);

        // Convert Entity to DTO and return
        return accessRevocationLogMapper.toDTO(save);
    }

    @Override
    public AccessRevocationLogDTO show(Long id) {
        AccessRevocationLog accessRevocationLogs = accessRevocationLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit clearance template not found with id: " + id));
        return accessRevocationLogMapper.toDTO(accessRevocationLogs);
    }

    @Override
    public AccessRevocationLogDTO update(Long id, AccessRevocationLogRequest request) {
        AccessRevocationLog accessRevocationLogs = new AccessRevocationLog();

        accessRevocationLogs.setSeparation(employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found")));

        accessRevocationLogs.setSeparation(employeeSeparationsRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Employee separation not found")));

        accessRevocationLogs.setRevokedBy(userRepository.findById(request.separationId())
                .orElseThrow(() -> new RuntimeException("Revoked by not found")));

        accessRevocationLogs.setSystemName(request.systemName());
        accessRevocationLogs.setAccessType(request.accessType());
        accessRevocationLogs.setRevocationDate(request.revocationDate());
        accessRevocationLogs.setAccessStatus(request.accessStatus());
        accessRevocationLogs.setErrorMessage(request.errorMessage());

        accessRevocationLogs.setRetryCount(request.retryCount());

        // Save entity
        AccessRevocationLog update = accessRevocationLogRepository.save(accessRevocationLogs);

        // Convert Entity to DTO and return
        return accessRevocationLogMapper.toDTO(update);
    }

    @Override
    public void destroy(Long id) {
        AccessRevocationLog accessRevocationLogs = accessRevocationLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("exit clearance template not found"));

        accessRevocationLogRepository.delete(accessRevocationLogs);
    }

}
