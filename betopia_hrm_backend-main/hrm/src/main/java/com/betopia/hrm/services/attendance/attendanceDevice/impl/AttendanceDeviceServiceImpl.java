package com.betopia.hrm.services.attendance.attendanceDevice.impl;

import com.betopia.hrm.domain.admin.repository.LocationRepository;
import com.betopia.hrm.domain.attendance.entity.AttendanceDevice;
import com.betopia.hrm.domain.attendance.entity.AttendanceDeviceCategory;
import com.betopia.hrm.domain.attendance.repository.AttendanceDeviceCategoryRepository;
import com.betopia.hrm.domain.attendance.repository.AttendanceDeviceRepository;
import com.betopia.hrm.domain.attendance.request.AttendanceDeviceRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceCategoryDTO;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceDTO;
import com.betopia.hrm.domain.dto.attendance.mapper.AttendanceDeviceMapper;
import com.betopia.hrm.services.attendance.attendanceDevice.AttendanceDeviceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceDeviceServiceImpl implements AttendanceDeviceService {

    private final AttendanceDeviceRepository attendanceDeviceRepository;

    private final AttendanceDeviceCategoryRepository attendanceDeviceCategoryRepository;

    private final LocationRepository locationRepository;

    private final AttendanceDeviceMapper attendanceDeviceMapper;

    public AttendanceDeviceServiceImpl(AttendanceDeviceRepository attendanceDeviceRepository, AttendanceDeviceCategoryRepository attendanceDeviceCategoryRepository, LocationRepository locationRepository, AttendanceDeviceMapper attendanceDeviceMapper) {
        this.attendanceDeviceRepository = attendanceDeviceRepository;
        this.attendanceDeviceCategoryRepository = attendanceDeviceCategoryRepository;
        this.locationRepository = locationRepository;
        this.attendanceDeviceMapper = attendanceDeviceMapper;
    }

    @Override
    public PaginationResponse<AttendanceDeviceDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<AttendanceDevice> attendanceDevicePage = attendanceDeviceRepository.findAll(pageable);

        // Get content from page
        List<AttendanceDevice> attendanceDevices  = attendanceDevicePage.getContent();

        // Convert entities to DTOs using MapStruct
        List<AttendanceDeviceDTO> attendanceDeviceDTOS = attendanceDeviceMapper.toDTOList(attendanceDevices);

        // Create pagination response
        PaginationResponse<AttendanceDeviceDTO> response = new PaginationResponse<>();
        response.setData(attendanceDeviceDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All attendance device fetched successfully");

        // Set links
        Links links = Links.fromPage(attendanceDevicePage, "/attendance-device");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(attendanceDevicePage, "/attendance-device");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<AttendanceDeviceDTO> getAllAttendanceDevices() {
        List<AttendanceDevice> attendanceDevices = attendanceDeviceRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));

        return attendanceDeviceMapper.toDTOList(attendanceDevices);
    }

    @Override
    public AttendanceDeviceDTO store(AttendanceDeviceRequest request) {
        AttendanceDevice attendanceDevice = new AttendanceDevice();

        attendanceDevice.setCategory(
                attendanceDeviceCategoryRepository.findById(request.categoryId()).orElseThrow(() -> new RuntimeException("Attendance device category not foudn"))
        );

        attendanceDevice.setLocation(
                locationRepository.findById(request.locationId()).orElseThrow(() -> new RuntimeException("Location Id is not found"))
        );

        attendanceDevice.setDeviceName(request.deviceName());
        attendanceDevice.setDeviceType(request.deviceType());
        attendanceDevice.setSerialNumber(request.serialNumber());
        attendanceDevice.setIpAddress(request.ipAddress());
        attendanceDevice.setMacAddress(request.macAddress());
        attendanceDevice.setFirmwareVersion(request.firmwareVersion());
        attendanceDevice.setDeviceCount(request.deviceCount());
        attendanceDevice.setStatus(request.status());

        AttendanceDevice attendanceDeviceSaved = attendanceDeviceRepository.save(attendanceDevice);

        return attendanceDeviceMapper.toDTO(attendanceDeviceSaved);
    }

    @Override
    public AttendanceDeviceDTO show(Long attendanceDeviceId) {
        AttendanceDevice attendanceDevice = attendanceDeviceRepository.findById(attendanceDeviceId)
                .orElseThrow(() -> new RuntimeException("Attendance device not found " + attendanceDeviceId));


        return attendanceDeviceMapper.toDTO(attendanceDevice);
    }

    @Override
    public AttendanceDeviceDTO update(Long attendanceDeviceId, AttendanceDeviceRequest request) {
        AttendanceDevice attendanceDevice = attendanceDeviceRepository.findById(attendanceDeviceId)
                .orElseThrow(() -> new RuntimeException("Attendance device not found " + attendanceDeviceId));


        attendanceDevice.setCategory(
                attendanceDeviceCategoryRepository.findById(request.categoryId()).orElseThrow(() -> new RuntimeException("Attendance device category not foudn"))
        );

        attendanceDevice.setLocation(
                locationRepository.findById(request.locationId()).orElseThrow(() -> new RuntimeException("Location Id is not found"))
        );

        attendanceDevice.setDeviceName(request.deviceName() != null ? request.deviceName() : attendanceDevice.getDeviceName());
        attendanceDevice.setDeviceType(request.deviceType() != null ? request.deviceType() : attendanceDevice.getDeviceType());
        attendanceDevice.setSerialNumber(request.serialNumber() != null ? request.serialNumber() : attendanceDevice.getSerialNumber());
        attendanceDevice.setIpAddress(request.ipAddress() != null ? request.ipAddress() : attendanceDevice.getIpAddress());
        attendanceDevice.setMacAddress(request.macAddress() != null ? request.macAddress() : attendanceDevice.getMacAddress());
        attendanceDevice.setFirmwareVersion(request.firmwareVersion() != null ? request.firmwareVersion() : attendanceDevice.getFirmwareVersion());
        attendanceDevice.setDeviceCount(request.deviceCount() != null ? request.deviceCount() : attendanceDevice.getDeviceCount());
        attendanceDevice.setStatus(request.status());

        AttendanceDevice attendanceDeviceUpdated = attendanceDeviceRepository.save(attendanceDevice);

        return attendanceDeviceMapper.toDTO(attendanceDeviceUpdated);
    }

    @Override
    public void destroy(Long attendanceDeviceId) {
        AttendanceDevice attendanceDevice = attendanceDeviceRepository.findById(attendanceDeviceId)
                .orElseThrow(() -> new RuntimeException("Attendance device not found " + attendanceDeviceId));

        if(attendanceDevice.getId() == attendanceDeviceId)
            attendanceDeviceRepository.deleteById(attendanceDeviceId);
        else
            throw new RuntimeException("Attendance device not found");
    }
}
