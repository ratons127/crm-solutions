package com.betopia.hrm.services.attendance.attendancepolicy.impl;


import com.betopia.hrm.domain.attendance.entity.AttendancePolicy;
import com.betopia.hrm.domain.attendance.entity.ShiftAssignments;
import com.betopia.hrm.domain.attendance.exception.AttendanceDeviceAssignNotFoundException;
import com.betopia.hrm.domain.attendance.exception.AttendancePolicyNotFoundException;
import com.betopia.hrm.domain.attendance.exception.ShiftAssignmentsNotFound;
import com.betopia.hrm.domain.attendance.repository.AttendancePolicyRepository;
import com.betopia.hrm.domain.attendance.request.AttendancePolicyRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendancePolicyDTO;
import com.betopia.hrm.domain.dto.attendance.mapper.AttendancePolicyMapper;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.services.attendance.attendancepolicy.AttendancePolicyService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AttendancePolicyServiceImpl implements AttendancePolicyService {

    private final AttendancePolicyRepository attendancePolicyRepository;
    private final AttendancePolicyMapper attendancePolicyMapper;
    private final CompanyRepository companyRepository;

    public AttendancePolicyServiceImpl(AttendancePolicyRepository attendancePolicyRepository,
                                       AttendancePolicyMapper attendancePolicyMapper, CompanyRepository companyRepository){
        this.attendancePolicyRepository = attendancePolicyRepository;
        this.attendancePolicyMapper = attendancePolicyMapper;
        this.companyRepository = companyRepository;
    }

    @Override
    public PaginationResponse<AttendancePolicyDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<AttendancePolicy> poloicyPage = attendancePolicyRepository.findAll(pageable);

        // Get content from page
        List<AttendancePolicy> attendanceDeviceAssigns  = poloicyPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<AttendancePolicyDTO> attendancePolicyDTOS = attendancePolicyMapper.toDTOList(attendanceDeviceAssigns);

        // Create pagination response
        PaginationResponse<AttendancePolicyDTO> response = new PaginationResponse<>();
        response.setData(attendancePolicyDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All attendance policy fetched successfully");

        // Set links
        Links links = Links.fromPage(poloicyPage, "/attendance-policy");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(poloicyPage, "/attendance-policy");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<AttendancePolicyDTO> getAll() {
        List<AttendancePolicy> attendancePolicies = attendancePolicyRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return attendancePolicyMapper.toDTOList(attendancePolicies);
    }

    @Override
    public AttendancePolicyDTO store(AttendancePolicyRequest request) {
        AttendancePolicy policy = new AttendancePolicy();

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + request.companyId()));

        boolean exists = attendancePolicyRepository.existsByCompanyId(company.getId());
        if (exists) {
            throw new IllegalStateException("Company with id " + company.getId() + " already has an attendance policy.");
        }

        System.err.println("company" + company.getId());

        policy.setCompanyId(company.getId());
        policy.setEffectiveFrom(LocalDate.now());
        policy.setEffectiveTo(request.effectiveTo());
        policy.setGraceInMinutes(request.graceInMinutes());
        policy.setGraceOutMinutes(request.graceOutMinutes());
        policy.setLateThresholdMinutes(request.lateThresholdMinutes());
        policy.setEarlyLeaveThresholdMinutes(request.earlyLeaveThresholdMinutes());
        policy.setMinWorkMinutes(request.minWorkMinutes());
        policy.setHalfDayThresholdMinutes(request.halfDayThresholdMinutes());
        policy.setAbsenceThresholdMinutes(request.absenceThresholdMinutes());
        policy.setMovementEnabled(request.movementEnabled());
        policy.setMovementAllowMinutes(request.movementAllowMinutes() != null ? request.movementAllowMinutes() : 0);
        policy.setNotes(request.notes());
        policy.setStatus(request.status());

        AttendancePolicy save = attendancePolicyRepository.save(policy);

        return attendancePolicyMapper.toDTO(save);
    }

    @Override
    public AttendancePolicyDTO show(Long Id) {
        AttendancePolicy attendancePolicies = attendancePolicyRepository.findById(Id)
                .orElseThrow(() -> new AttendancePolicyNotFoundException("Company policies not found " + Id));

        return attendancePolicyMapper.toDTO(attendancePolicies);
    }

    @Override
    public AttendancePolicyDTO update(Long Id, AttendancePolicyRequest request) {
        AttendancePolicy policy = attendancePolicyRepository.findById(Id)
                .orElseThrow(() -> new AttendanceDeviceAssignNotFoundException("Attendance policy not found " + Id));

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new EntityNotFoundException("Company not found with id: " + request.companyId()));

        policy.setCompanyName(company.getName());
        policy.setCompanyId(company.getId());
        policy.setEffectiveFrom(request.effectiveFrom());
        policy.setEffectiveTo(request.effectiveTo());
        policy.setGraceInMinutes(request.graceInMinutes());
        policy.setGraceOutMinutes(request.graceOutMinutes());
        policy.setLateThresholdMinutes(request.lateThresholdMinutes());
        policy.setEarlyLeaveThresholdMinutes(request.earlyLeaveThresholdMinutes());
        policy.setMinWorkMinutes(request.minWorkMinutes());
        policy.setHalfDayThresholdMinutes(request.halfDayThresholdMinutes());
        policy.setAbsenceThresholdMinutes(request.absenceThresholdMinutes());
        policy.setMovementEnabled(request.movementEnabled());
        policy.setMovementAllowMinutes(request.movementAllowMinutes() != null ? request.movementAllowMinutes() : 0);
        policy.setNotes(request.notes());
        policy.setStatus(request.status());

        AttendancePolicy save = attendancePolicyRepository.save(policy);

        return attendancePolicyMapper.toDTO(save);
    }

    @Override
    public void destroy(Long Id) {
        AttendancePolicy policy = attendancePolicyRepository.findById(Id).orElseThrow(() ->
                new EntityNotFoundException("Attendance policy not found with id: " + Id));
        attendancePolicyRepository.delete(policy);
    }
}
