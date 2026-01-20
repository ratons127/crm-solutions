package com.betopia.hrm.services.attendance.attendanceCategory.impl;

import com.betopia.hrm.domain.attendance.entity.AttendanceDeviceCategory;
import com.betopia.hrm.domain.attendance.exception.AttendanceDeviceCategoryNotFoundException;
import com.betopia.hrm.domain.attendance.repository.AttendanceDeviceCategoryRepository;
import com.betopia.hrm.domain.attendance.request.AttendanceDeviceCategoryRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceCategoryDTO;
import com.betopia.hrm.domain.dto.attendance.mapper.AttendanceDeviceCategoryMapper;
import com.betopia.hrm.services.attendance.attendanceCategory.AttendanceDeviceCategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceDeviceCategoryServiceImpl implements AttendanceDeviceCategoryService {

    private final AttendanceDeviceCategoryRepository attendanceDeviceCategoryRepository;

    private final AttendanceDeviceCategoryMapper attendanceDeviceCategoryMapper;

    public AttendanceDeviceCategoryServiceImpl(AttendanceDeviceCategoryRepository attendanceDeviceCategoryRepository, AttendanceDeviceCategoryMapper attendanceDeviceCategoryMapper) {
        this.attendanceDeviceCategoryRepository = attendanceDeviceCategoryRepository;
        this.attendanceDeviceCategoryMapper = attendanceDeviceCategoryMapper;
    }

    @Override
    public PaginationResponse<AttendanceDeviceCategoryDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<AttendanceDeviceCategory> attendanceDeviceCategoryPage = attendanceDeviceCategoryRepository.findAll(pageable);

        // Get content from page
        List<AttendanceDeviceCategory> attendanceDeviceCategories  = attendanceDeviceCategoryPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<AttendanceDeviceCategoryDTO> attendanceDeviceCategoryDTOS = attendanceDeviceCategoryMapper.toDTOList(attendanceDeviceCategories);

        // Create pagination response
        PaginationResponse<AttendanceDeviceCategoryDTO> response = new PaginationResponse<>();
        response.setData(attendanceDeviceCategoryDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All attendance device category fetched successfully");

        // Set links
        Links links = Links.fromPage(attendanceDeviceCategoryPage, "/attendance-device-categories");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(attendanceDeviceCategoryPage, "/attendance-device-categories");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<AttendanceDeviceCategoryDTO> getAllAttendanceDeviceCategories() {
        List<AttendanceDeviceCategory> attendanceDeviceCategories = attendanceDeviceCategoryRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        return attendanceDeviceCategoryMapper.toDTOList(attendanceDeviceCategories);
    }

    @Override
    public AttendanceDeviceCategoryDTO store(AttendanceDeviceCategoryRequest request) {
        AttendanceDeviceCategory attendanceDeviceCategory = new AttendanceDeviceCategory();

        attendanceDeviceCategory.setName(request.name());
        attendanceDeviceCategory.setCommunicationType(request.communicationType());
        attendanceDeviceCategory.setBiometricMode(request.biometricMode());
        attendanceDeviceCategory.setDescription(request.description());
        attendanceDeviceCategory.setStatus(request.status());

        AttendanceDeviceCategory attendanceDeviceCategorySaved = attendanceDeviceCategoryRepository.save(attendanceDeviceCategory);

        return attendanceDeviceCategoryMapper.toDTO(attendanceDeviceCategorySaved);
    }

    @Override
    public AttendanceDeviceCategoryDTO show(Long attendanceDeviceCategoryId) {
        AttendanceDeviceCategory attendanceDeviceCategory = attendanceDeviceCategoryRepository.findById(attendanceDeviceCategoryId)
                .orElseThrow(() -> new AttendanceDeviceCategoryNotFoundException("Attendance device category not found " + attendanceDeviceCategoryId));

        return attendanceDeviceCategoryMapper.toDTO(attendanceDeviceCategory);
    }

    @Override
    public AttendanceDeviceCategoryDTO update(Long attendanceDeviceCategoryId, AttendanceDeviceCategoryRequest request) {
        AttendanceDeviceCategory attendanceDeviceCategory = attendanceDeviceCategoryRepository.findById(attendanceDeviceCategoryId)
                .orElseThrow(() -> new AttendanceDeviceCategoryNotFoundException("Attendance device category not found " + attendanceDeviceCategoryId));

        attendanceDeviceCategory.setName(request.name() != null ? request.name() : attendanceDeviceCategory.getName());
        attendanceDeviceCategory.setCommunicationType(request.communicationType() != null ? request.communicationType() : attendanceDeviceCategory.getCommunicationType());
        attendanceDeviceCategory.setBiometricMode(request.biometricMode() != null ? request.biometricMode() : attendanceDeviceCategory.getBiometricMode());
        attendanceDeviceCategory.setDescription(request.description() != null ? request.description() : attendanceDeviceCategory.getDescription());
        attendanceDeviceCategory.setStatus(request.status());

        AttendanceDeviceCategory attendanceDeviceCategoryUpdate = attendanceDeviceCategoryRepository.save(attendanceDeviceCategory);

        return attendanceDeviceCategoryMapper.toDTO(attendanceDeviceCategoryUpdate);
    }

    @Override
    public void destroy(Long attendanceDeviceCategoryId) {
        AttendanceDeviceCategory attendanceDeviceCategory = attendanceDeviceCategoryRepository.findById(attendanceDeviceCategoryId)
                .orElseThrow(() -> new AttendanceDeviceCategoryNotFoundException("Attendance device category not found " + attendanceDeviceCategoryId));

        try {
            attendanceDeviceCategoryRepository.deleteById(attendanceDeviceCategoryId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete attendance device category Pattern with ID: " + attendanceDeviceCategoryId);
        }
    }
}
