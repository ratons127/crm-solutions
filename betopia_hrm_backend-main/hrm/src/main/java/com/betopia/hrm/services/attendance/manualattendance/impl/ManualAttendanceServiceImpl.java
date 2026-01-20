package com.betopia.hrm.services.attendance.manualattendance.impl;

import com.betopia.hrm.domain.attendance.entity.AttendancePolicy;
import com.betopia.hrm.domain.attendance.entity.ManualAttendance;
import com.betopia.hrm.domain.attendance.entity.Shift;
import com.betopia.hrm.domain.attendance.exception.AttendancePolicyNotFoundException;
import com.betopia.hrm.domain.attendance.exception.ManualAttendanceException;
import com.betopia.hrm.domain.attendance.exception.ShiftNotFoundException;
import com.betopia.hrm.domain.attendance.repository.AttendancePolicyRepository;
import com.betopia.hrm.domain.attendance.repository.ManualAttendanceRepository;
import com.betopia.hrm.domain.attendance.repository.ShiftRepository;
import com.betopia.hrm.domain.attendance.request.ManualAttendanceRequest;
import com.betopia.hrm.domain.attendance.specification.AttendanceSpecification;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ManualAttendanceDTO;
import com.betopia.hrm.domain.dto.attendance.mapper.ManualAttendanceMapper;
import com.betopia.hrm.domain.dto.leave.LeaveRequestDTO;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.specification.EmployeeSpecification;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.attendance.attendanceapproval.AttendanceApprovalService;
import com.betopia.hrm.services.attendance.attendancesummary.AttendanceSummaryService;
import com.betopia.hrm.services.attendance.manualattendance.ManualAttendanceService;
import com.betopia.hrm.webapp.util.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ManualAttendanceServiceImpl implements ManualAttendanceService {

    private final ManualAttendanceRepository manualAttendanceRepository;
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final AttendancePolicyRepository attendancePolicyRepository;
    private final ManualAttendanceMapper manualAttendanceMapper;
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final AttendanceSummaryService attendanceSummaryService;
    private final AttendanceApprovalService attendanceApprovalService;

    public ManualAttendanceServiceImpl(ManualAttendanceRepository manualAttendanceRepository,
                                       EmployeeRepository employeeRepository,
                                       CompanyRepository companyRepository,
                                       AttendancePolicyRepository attendancePolicyRepository,
                                       ManualAttendanceMapper manualAttendanceMapper,
                                       ShiftRepository shiftRepository, UserRepository userRepository,
                                       AttendanceSummaryService attendanceSummaryService,
                                       AttendanceApprovalService attendanceApprovalService){

        this.manualAttendanceRepository =  manualAttendanceRepository;
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.attendancePolicyRepository = attendancePolicyRepository;
        this.manualAttendanceMapper = manualAttendanceMapper;
        this.shiftRepository = shiftRepository;
        this.userRepository = userRepository;
        this.attendanceSummaryService =  attendanceSummaryService;
        this.attendanceApprovalService = attendanceApprovalService;
    }

    @Override
    public PaginationResponse<ManualAttendanceDTO> index(Sort.Direction direction, int page,
                                                         int perPage,String keyword,Long userId) {
        Specification<ManualAttendance> spec =
                AttendanceSpecification.globalSearch(keyword);
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<ManualAttendance> attendancePage = manualAttendanceRepository.findAll(spec,pageable);

        // Get content from page
//        List<ManualAttendance> manualAttendances  = attendancePage.getContent();

        List<ManualAttendanceDTO> manualAttendances = attendancePage.getContent()
                .stream()
                .filter(lr -> userId == null || lr.getCreatedBy().equals(userId)) // filter if userId is provided
                .map(manualAttendanceMapper::toDTO)
                .toList();

        // Convert entities to DTOs using MapStruct
//        List<ManualAttendanceDTO> manualAttendancesDTOS = manualAttendanceMapper.toDTOList(manualAttendances);

        // Create pagination response
        PaginationResponse<ManualAttendanceDTO> response = new PaginationResponse<>();
        response.setData(manualAttendances);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All manual attendance fetched successfully");

        // Set links
        Links links = Links.fromPage(attendancePage, "/manual-attendance");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(attendancePage, "/manual-attendance");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<ManualAttendanceDTO> getAll(Long userId) {
        return manualAttendanceRepository.findAll(Sort.by(Sort.Direction.DESC, "id")) // fetch all
                .stream()
                .filter(lr -> userId == null || lr.getCreatedBy().equals(userId)) // filter if userId is provided
                .map(manualAttendanceMapper::toDTO)
                .toList();
    }

    @Override
    public ManualAttendanceDTO store(ManualAttendanceRequest request) {
        ManualAttendance attendance = new ManualAttendance();

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + request.employeeId()));

        if (employee.getShift() == null) {
            throw new ShiftNotFoundException("Shift not assigned to employee with ID: " + employee.getId());
        }

        boolean exists = manualAttendanceRepository.existsByEmployeeIdAndAttendanceDate(employee.getId(), request.attendanceDate());
        if (exists) {
            throw new IllegalArgumentException(
                    "Manual attendance already exists for employee with ID: " + employee.getId() + " on date: " + request.attendanceDate()
            );
        }

        User users = null;
        String identifier = AuthUtils.getCurrentUsername();

        if (identifier != null && !identifier.isEmpty()) {
            users = userRepository.findByEmailOrPhone(identifier, identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email/phone: " + identifier));
        } else {
            throw new EntityNotFoundException("No authenticated user found for submission.");
        }

//        User users = userRepository.findById(request.submittedById())
//                .orElseThrow(() -> new EntityNotFoundException("users not found with id: " + request.submittedById()));

        attendance.setEmployeeId(employee.getId());
        attendance.setAttendanceDate(request.attendanceDate());
        attendance.setInTime(request.inTime());
        attendance.setOutTime(request.outTime());
        attendance.setAdjustmentType(request.adjustmentType());
        attendance.setReason(request.reason());
        attendance.setSubmittedBy(users.getId());
        attendance.setSubmittedAt(request.submittedAt());
        attendance.setSourceChannel(request.sourceChannel());
        attendance.setIsLocked(request.isLocked());
        attendance.setManualAttendanceStatus(request.manualAttendanceStatus());
        attendance.setCreatedBy(users.getId());
        attendance.setCreatedDate(LocalDateTime.now());

        ManualAttendance savedManualAttendance = manualAttendanceRepository.save(attendance);

        attendanceSummaryService.createInitialAttendanceSummary(savedManualAttendance,employee.getEmployeeSerialId());
        attendanceApprovalService.createInitialAttendanceApproval(savedManualAttendance);

        return manualAttendanceMapper.toDTO(savedManualAttendance);
    }

    @Override
    public ManualAttendanceDTO show(Long Id) {
        ManualAttendance attendance = manualAttendanceRepository.findById(Id)
                .orElseThrow(() -> new ManualAttendanceException("Manual attendance not found " + Id));

        return manualAttendanceMapper.toDTO(attendance);
    }

    @Override
    public ManualAttendanceDTO update(Long Id, ManualAttendanceRequest request) {
        ManualAttendance attendance = manualAttendanceRepository.findById(Id)
                .orElseThrow(() -> new ManualAttendanceException("Manual attendance not found " + Id));

        User users = null;
        String identifier = AuthUtils.getCurrentUsername();

        if (identifier != null && !identifier.isEmpty()) {
            users = userRepository.findByEmailOrPhone(identifier, identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email/phone: " + identifier));
        } else {
            throw new EntityNotFoundException("No authenticated user found for submission.");
        }

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + request.employeeId()));

        User submiitedby = userRepository.findById(request.submittedById())
                .orElseThrow(() -> new EntityNotFoundException("users not found with id: " + request.submittedById()));


        attendance.setAttendanceDate(request.attendanceDate());
        attendance.setInTime(request.inTime());
        attendance.setOutTime(request.outTime());
        attendance.setAdjustmentType(request.adjustmentType());
        attendance.setReason(request.reason());
        attendance.setSubmittedBy(submiitedby.getId());
        attendance.setSubmittedAt(request.submittedAt());
        attendance.setSourceChannel(request.sourceChannel());
        attendance.setIsLocked(request.isLocked());
        attendance.setManualAttendanceStatus(request.manualAttendanceStatus());
        attendance.setLastModifiedBy(users.getId());
        attendance.setLastModifiedDate(LocalDateTime.now());

        ManualAttendance savedManualAttendance = manualAttendanceRepository.save(attendance);

        return manualAttendanceMapper.toDTO(savedManualAttendance);
    }

    @Override
    public void destroy(Long Id) {
        ManualAttendance attendance = manualAttendanceRepository.findById(Id).orElseThrow(() ->
                new EntityNotFoundException("Manual attendance not found with id: " + Id));
        manualAttendanceRepository.delete(attendance);
    }
}
