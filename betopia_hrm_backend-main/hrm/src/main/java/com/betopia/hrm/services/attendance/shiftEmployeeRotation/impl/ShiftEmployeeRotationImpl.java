package com.betopia.hrm.services.attendance.shiftEmployeeRotation.impl;

import com.betopia.hrm.domain.attendance.entity.ShiftEmployeeRotation;
import com.betopia.hrm.domain.attendance.exception.ShiftEmployeeRotationNotFoundException;
import com.betopia.hrm.domain.attendance.repository.ShiftEmployeeRotationRepository;
import com.betopia.hrm.domain.attendance.repository.ShiftRotationPatternRepository;
import com.betopia.hrm.domain.attendance.request.ShiftEmployeeRotationRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ShiftEmployeeRotationDTO;
import com.betopia.hrm.domain.dto.attendance.mapper.ShiftEmployeeRotationMapper;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.services.attendance.shiftEmployeeRotation.ShiftEmployeeRotationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftEmployeeRotationImpl implements ShiftEmployeeRotationService {

    private final ShiftEmployeeRotationRepository shiftEmployeeRotationRepository;

    private final EmployeeRepository employeeRepository;

    private final ShiftRotationPatternRepository shiftRotationPatternRepository;

    private final ShiftEmployeeRotationMapper shiftEmployeeRotationMapper;

    public ShiftEmployeeRotationImpl(ShiftEmployeeRotationRepository shiftEmployeeRotationRepository, EmployeeRepository employeeRepository, ShiftRotationPatternRepository shiftRotationPatternRepository, ShiftEmployeeRotationMapper shiftEmployeeRotationMapper) {
        this.shiftEmployeeRotationRepository = shiftEmployeeRotationRepository;
        this.employeeRepository = employeeRepository;
        this.shiftRotationPatternRepository = shiftRotationPatternRepository;
        this.shiftEmployeeRotationMapper = shiftEmployeeRotationMapper;
    }

    @Override
    public PaginationResponse<ShiftEmployeeRotationDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<ShiftEmployeeRotation> shiftEmployeeRotationPage = shiftEmployeeRotationRepository.findAll(pageable);

        // Get content from page
        List<ShiftEmployeeRotation> shiftEmployeeRotations = shiftEmployeeRotationPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<ShiftEmployeeRotationDTO> shiftEmployeeRotationDTOS = shiftEmployeeRotationMapper.toDTOList(shiftEmployeeRotations);

        // Create pagination response
        PaginationResponse<ShiftEmployeeRotationDTO> response = new PaginationResponse<>();
        response.setData(shiftEmployeeRotationDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All shift Employee Rotation fetched successfully");

        // Set links
        Links links = Links.fromPage(shiftEmployeeRotationPage, "/shift-employee-rotation");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(shiftEmployeeRotationPage, "/shift-employee-rotation");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<ShiftEmployeeRotationDTO> getAllShiftEmployeeRotation() {
        List<ShiftEmployeeRotation> shiftEmployeeRotations = shiftEmployeeRotationRepository.findAll();
        return shiftEmployeeRotationMapper.toDTOList(shiftEmployeeRotations);
    }

    @Override
    public ShiftEmployeeRotationDTO store(ShiftEmployeeRotationRequest request) {
        ShiftEmployeeRotation shiftEmployeeRotation = new ShiftEmployeeRotation();

        shiftEmployeeRotation.setEmployee(
                employeeRepository.findById(request.employeeId()).orElseThrow(() -> new RuntimeException("Employee not found"))
        );
        shiftEmployeeRotation.setPattern(
                shiftRotationPatternRepository.findById(request.patternId()).orElseThrow(() -> new RuntimeException("Shift Rotation Pattern not found"))
        );
        shiftEmployeeRotation.setStartDate(request.startDate());
        shiftEmployeeRotation.setEndDate(request.endDate());
        shiftEmployeeRotation.setCycleStartDay(request.cycleStartDay());
        shiftEmployeeRotation.setStatus(request.status());

        ShiftEmployeeRotation savedShiftEmployeeRotation = shiftEmployeeRotationRepository.save(shiftEmployeeRotation);

        return shiftEmployeeRotationMapper.toDTO(savedShiftEmployeeRotation);
    }

    @Override
    public ShiftEmployeeRotationDTO show(Long shiftEmployeeRotationId) {
        ShiftEmployeeRotation shiftEmployeeRotation = shiftEmployeeRotationRepository.findById(shiftEmployeeRotationId)
                .orElseThrow(() -> new ShiftEmployeeRotationNotFoundException("Shift employee rotation not found " + shiftEmployeeRotationId));

        return shiftEmployeeRotationMapper.toDTO(shiftEmployeeRotation);
    }

    @Override
    public ShiftEmployeeRotationDTO update(Long shiftEmployeeRotationId, ShiftEmployeeRotationRequest request) {
        ShiftEmployeeRotation shiftEmployeeRotation = shiftEmployeeRotationRepository.findById(shiftEmployeeRotationId)
                .orElseThrow(() -> new ShiftEmployeeRotationNotFoundException("Shift employee rotation not found " + shiftEmployeeRotationId));

        shiftEmployeeRotation.setEmployee(
                employeeRepository.findById(request.employeeId()).orElseThrow(() -> new RuntimeException("Employee not found"))
        );
        shiftEmployeeRotation.setPattern(
                shiftRotationPatternRepository.findById(request.patternId()).orElseThrow(() -> new RuntimeException("Shift Rotation Pattern not found"))
        );
        shiftEmployeeRotation.setStartDate(request.startDate() != null ? request.startDate() : shiftEmployeeRotation.getStartDate());
        shiftEmployeeRotation.setEndDate(request.endDate() != null ? request.endDate() : shiftEmployeeRotation.getEndDate());
        shiftEmployeeRotation.setCycleStartDay(request.cycleStartDay() != null ? request.cycleStartDay() : shiftEmployeeRotation.getCycleStartDay());
        shiftEmployeeRotation.setStatus(request.status() != null ? request.status() : shiftEmployeeRotation.getStatus());

        ShiftEmployeeRotation savedShiftEmployeeRotation = shiftEmployeeRotationRepository.save(shiftEmployeeRotation);

        return shiftEmployeeRotationMapper.toDTO(savedShiftEmployeeRotation);
    }

    @Override
    public void destroy(Long shiftEmployeeRotationId) {
        ShiftEmployeeRotation shiftEmployeeRotation = shiftEmployeeRotationRepository.findById(shiftEmployeeRotationId)
                .orElseThrow(() -> new ShiftEmployeeRotationNotFoundException("Shift employee rotation not found " + shiftEmployeeRotationId));

        try {
            shiftEmployeeRotationRepository.deleteById(shiftEmployeeRotationId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete Shift Rotation Pattern with ID: " + shiftEmployeeRotationId);
        }
    }
}
