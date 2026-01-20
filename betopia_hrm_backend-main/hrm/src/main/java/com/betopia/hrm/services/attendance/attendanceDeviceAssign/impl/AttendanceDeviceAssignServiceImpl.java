package com.betopia.hrm.services.attendance.attendanceDeviceAssign.impl;

import com.betopia.hrm.domain.attendance.entity.AttendanceDeviceAssign;
import com.betopia.hrm.domain.attendance.exception.AttendanceDeviceAssignNotFoundException;
import com.betopia.hrm.domain.attendance.exception.AttendanceDeviceNotFoundException;
import com.betopia.hrm.domain.attendance.repository.AttendanceDeviceAssignRepository;
import com.betopia.hrm.domain.attendance.repository.AttendanceDeviceRepository;
import com.betopia.hrm.domain.attendance.request.AttendanceDeviceAssignRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceAssignDTO;
import com.betopia.hrm.domain.dto.attendance.mapper.AttendanceDeviceAssignMapper;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.services.attendance.attendanceDeviceAssign.AttendanceDeviceAssignService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceDeviceAssignServiceImpl implements AttendanceDeviceAssignService {

    private final AttendanceDeviceAssignRepository attendanceDeviceAssignRepository;

    private final EmployeeRepository employeeRepository;

    private final AttendanceDeviceRepository attendanceDeviceRepository;

    private final AttendanceDeviceAssignMapper attendanceDeviceAssignMapper;

    public AttendanceDeviceAssignServiceImpl(AttendanceDeviceAssignRepository attendanceDeviceAssignRepository, EmployeeRepository employeeRepository, AttendanceDeviceRepository attendanceDeviceRepository, AttendanceDeviceAssignMapper attendanceDeviceAssignMapper) {
        this.attendanceDeviceAssignRepository = attendanceDeviceAssignRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceDeviceRepository = attendanceDeviceRepository;
        this.attendanceDeviceAssignMapper = attendanceDeviceAssignMapper;
    }

    @Override
    public PaginationResponse<AttendanceDeviceAssignDTO> index(Sort.Direction direction, int page, int perPage) {

        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<AttendanceDeviceAssign> attendanceDeviceAssignPage = attendanceDeviceAssignRepository.findAll(pageable);

        // Get content from page
        List<AttendanceDeviceAssign> attendanceDeviceAssigns  = attendanceDeviceAssignPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<AttendanceDeviceAssignDTO> attendanceDeviceAssignDTOS = attendanceDeviceAssignMapper.toDTOList(attendanceDeviceAssigns);

        // Create pagination response
        PaginationResponse<AttendanceDeviceAssignDTO> response = new PaginationResponse<>();
        response.setData(attendanceDeviceAssignDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All attendance device assign fetched successfully");

        // Set links
        Links links = Links.fromPage(attendanceDeviceAssignPage, "/attendance-device-assign");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(attendanceDeviceAssignPage, "/attendance-device-assign");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<AttendanceDeviceAssignDTO> getAllAttendanceDeviceAssigns() {
        List<AttendanceDeviceAssign> attendanceDeviceAssigns = attendanceDeviceAssignRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return attendanceDeviceAssignMapper.toDTOList(attendanceDeviceAssigns);
    }

    @Override
    public AttendanceDeviceAssignDTO store(AttendanceDeviceAssignRequest request) {
        AttendanceDeviceAssign attendanceDeviceAssign = new AttendanceDeviceAssign();

        attendanceDeviceAssign.setAttendanceDevice(
                attendanceDeviceRepository.findById(request.attendanceDeviceId()).orElseThrow(() -> new AttendanceDeviceNotFoundException("Attendance device not found"))
        );

        attendanceDeviceAssign.setEmployee(
                employeeRepository.findById(request.employeeId()).orElseThrow(() -> new EmployeeNotFound("Employee not found"))
        );

        attendanceDeviceAssign.setDeviceUserId(request.deviceUserId());
        attendanceDeviceAssign.setAssignedBy(request.assignedBy());
        attendanceDeviceAssign.setAssignedAt(request.assignedAt());
        attendanceDeviceAssign.setNotes(request.notes());
        attendanceDeviceAssign.setStatus(request.status());

        AttendanceDeviceAssign attendanceDeviceAssignSaved = attendanceDeviceAssignRepository.save(attendanceDeviceAssign);

        return attendanceDeviceAssignMapper.toDTO(attendanceDeviceAssignSaved);
    }

    @Override
    public AttendanceDeviceAssignDTO show(Long attendanceDeviceAssignId) {
        AttendanceDeviceAssign attendanceDeviceAssign = attendanceDeviceAssignRepository.findById(attendanceDeviceAssignId)
                .orElseThrow(() -> new AttendanceDeviceAssignNotFoundException("Attendance device assign not found " + attendanceDeviceAssignId));

        return attendanceDeviceAssignMapper.toDTO(attendanceDeviceAssign);
    }

    @Override
    public AttendanceDeviceAssignDTO update(Long attendanceDeviceAssignId, AttendanceDeviceAssignRequest request) {
        AttendanceDeviceAssign attendanceDeviceAssign = attendanceDeviceAssignRepository.findById(attendanceDeviceAssignId)
                .orElseThrow(() -> new AttendanceDeviceAssignNotFoundException("Attendance device assign not found " + attendanceDeviceAssignId));

        attendanceDeviceAssign.setAttendanceDevice(
                attendanceDeviceRepository.findById(request.attendanceDeviceId()).orElseThrow(() -> new AttendanceDeviceNotFoundException("Attendance device not found"))
        );

        attendanceDeviceAssign.setEmployee(
                employeeRepository.findById(request.employeeId()).orElseThrow(() -> new EmployeeNotFound("Employee not found"))
        );

        attendanceDeviceAssign.setDeviceUserId(request.deviceUserId() != null ? request.deviceUserId() : attendanceDeviceAssign.getDeviceUserId());
        attendanceDeviceAssign.setAssignedBy(request.assignedBy() != null ? request.assignedBy() : attendanceDeviceAssign.getAssignedBy());
        attendanceDeviceAssign.setAssignedAt(request.assignedAt() != null ? request.assignedAt() : attendanceDeviceAssign.getAssignedAt());
        attendanceDeviceAssign.setNotes(request.notes() != null ? request.notes() : attendanceDeviceAssign.getNotes());
        attendanceDeviceAssign.setStatus(request.status());

        AttendanceDeviceAssign attendanceDeviceAssignUpdate = attendanceDeviceAssignRepository.save(attendanceDeviceAssign);

        return attendanceDeviceAssignMapper.toDTO(attendanceDeviceAssignUpdate);
    }

    @Override
    public void destroy(Long attendanceDeviceAssignId) {
        AttendanceDeviceAssign attendanceDeviceAssign = attendanceDeviceAssignRepository.findById(attendanceDeviceAssignId)
                .orElseThrow(() -> new AttendanceDeviceAssignNotFoundException("Attendance device assign not found " + attendanceDeviceAssignId));

        if(attendanceDeviceAssign.getId() == attendanceDeviceAssignId)
            attendanceDeviceAssignRepository.deleteById(attendanceDeviceAssignId);
        else
            throw new RuntimeException("Attendance device assign not found");
    }
}
