package com.betopia.hrm.services.attendance.attendancesummary.impl;

import com.betopia.hrm.domain.attendance.entity.AttendancePolicy;
import com.betopia.hrm.domain.attendance.entity.AttendanceSummary;
import com.betopia.hrm.domain.attendance.entity.ManualAttendance;
import com.betopia.hrm.domain.attendance.entity.Shift;
import com.betopia.hrm.domain.attendance.exception.AttendancePolicyNotFoundException;
import com.betopia.hrm.domain.attendance.exception.AttendanceSummaryNotFoundException;
import com.betopia.hrm.domain.attendance.exception.ShiftNotFoundException;
import com.betopia.hrm.domain.attendance.repository.AttendancePolicyRepository;
import com.betopia.hrm.domain.attendance.repository.AttendanceSummaryRepository;
import com.betopia.hrm.domain.attendance.repository.ManualAttendanceRepository;
import com.betopia.hrm.domain.attendance.repository.ShiftRepository;
import com.betopia.hrm.domain.attendance.request.AttendanceSummaryRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceStatusDTO;
import com.betopia.hrm.domain.dto.attendance.AttendanceSummaryDTO;
import com.betopia.hrm.domain.dto.attendance.DeviceTypeDTO;
import com.betopia.hrm.domain.dto.attendance.mapper.AttendanceSummaryMapper;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.attendance.attendancesummary.AttendanceSummaryService;
import com.betopia.hrm.webapp.util.AttendanceUtil;
import com.betopia.hrm.webapp.util.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import static com.betopia.hrm.domain.attendance.enums.AttendanceType.MANUAL;

@Service
public class AttendanceSummaryServiceImpl implements AttendanceSummaryService {

    private final CompanyRepository companyRepository;
    private final AttendanceSummaryRepository attendanceSummaryRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendancePolicyRepository attendancePolicyRepository;
    private final ShiftRepository shiftRepository;
    private final ManualAttendanceRepository manualAttendanceRepository;
    private final AttendanceSummaryMapper attendanceSummaryMapper;
    private final AttendanceUtil attendanceUtil;
    private final UserRepository userRepository;


    public AttendanceSummaryServiceImpl(CompanyRepository companyRepository,
                                        AttendanceSummaryRepository attendanceSummaryRepository,
                                        EmployeeRepository employeeRepository,
                                        AttendancePolicyRepository attendancePolicyRepository,
                                        ShiftRepository shiftRepository,
                                        AttendanceSummaryMapper attendanceSummaryMapper,
                                        ManualAttendanceRepository manualAttendanceRepository,
                                        AttendanceUtil attendanceUtil, UserRepository userRepository) {
        this.companyRepository = companyRepository;
        this.attendanceSummaryRepository = attendanceSummaryRepository;
        this.employeeRepository = employeeRepository;
        this.attendancePolicyRepository = attendancePolicyRepository;
        this.shiftRepository = shiftRepository;
        this.attendanceSummaryMapper = attendanceSummaryMapper;
        this.manualAttendanceRepository = manualAttendanceRepository;
        this.attendanceUtil = attendanceUtil;
        this.userRepository = userRepository;
    }

    @Override
    public PaginationResponse<AttendanceSummaryDTO> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<AttendanceSummary> AttendanceSummaryPage = attendanceSummaryRepository.findAll(pageable);
        List<AttendanceSummary> AttendanceSummaryDTO = AttendanceSummaryPage.getContent();
        List<AttendanceSummaryDTO> AttendanceSummaryDTOs = attendanceSummaryMapper.toDTOList(AttendanceSummaryDTO);
        PaginationResponse<AttendanceSummaryDTO> response = new PaginationResponse<>();
        response.setData(AttendanceSummaryDTOs);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Attendance Summary fetched successfully");

        Links links = Links.fromPage(AttendanceSummaryPage, "/attendance-summary");
        response.setLinks(links);

        Meta meta = Meta.fromPage(AttendanceSummaryPage, "/attendance-Summary");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<AttendanceSummaryDTO> getAllAttendanceSummaries() {
        List<AttendanceSummary> attendanceSummaries = attendanceSummaryRepository.findAll();
        return attendanceSummaryMapper.toDTOList(attendanceSummaries);
    }

    @Override
    public AttendanceSummaryDTO store(AttendanceSummaryRequest request) {
        AttendanceSummary attendanceSummary = new AttendanceSummary();


        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyNotFound("Company not found"));

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new EmployeeNotFound("Employee not found"));

        AttendancePolicy attendancePolicy = attendancePolicyRepository.findById(request.attendancePolicyId())
                .orElseThrow(() -> new AttendancePolicyNotFoundException("Attendance Policy not found"));

        Shift shift = shiftRepository.findById(request.shiftId())
                .orElseThrow(() -> new ShiftNotFoundException("Shift not found"));


        attendanceSummary.setCompanyId(company.getId());
        attendanceSummary.setEmployeeId(employee.getId());
        attendanceSummary.setAttendancePolicyId(attendancePolicy.getId());
        attendanceSummary.setShiftId(shift.getId());

        attendanceSummary.setWorkDate(request.workDate());
        attendanceSummary.setInTime(request.inTime() != null ? request.inTime() : null);
        attendanceSummary.setOutTime(request.outTime() != null ? request.outTime() : null);
        attendanceSummary.setTotalWorkMinutes(request.totalWorkMinutes());
        attendanceSummary.setLateMinutes(request.lateMinutes());
        attendanceSummary.setEarlyLeaveMinutes(request.earlyLeaveMinutes());
        attendanceSummary.setOvertimeMinutes(request.overtimeMinutes());

        attendanceSummary.setDayStatus(request.dayStatus());
        attendanceSummary.setAttendanceType(request.attendanceType());
        attendanceSummary.setManualRequestId(request.manualRequestId());
        attendanceSummary.setRemarks(request.remarks());
        attendanceSummary.setComputedAt(request.computedAt());
        attendanceSummary.setIsLocked(request.isLocked());


        // Save entity
        AttendanceSummary savedAttendanceSummary= attendanceSummaryRepository.save(attendanceSummary);

        return attendanceSummaryMapper.toDTO(savedAttendanceSummary);
    }

    @Override
    public AttendanceSummaryDTO show(Long attendanceSummaryId) {
        AttendanceSummary attendanceSummary = attendanceSummaryRepository.findById(attendanceSummaryId)
                .orElseThrow(() -> new RuntimeException("Attendance Summary  not found with id: " + attendanceSummaryId));
        return attendanceSummaryMapper.toDTO(attendanceSummary);
    }

    @Override
    public AttendanceSummaryDTO update(Long attendanceSummaryId, AttendanceSummaryRequest request) {
        AttendanceSummary attendanceSummary = attendanceSummaryRepository.findById(attendanceSummaryId)
                .orElseThrow(() -> new AttendanceSummaryNotFoundException("Attendance Summary not found with id: " + attendanceSummaryId));

        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyNotFound("Company not found"));

        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new EmployeeNotFound("Employee not found"));

        AttendancePolicy attendancePolicy = attendancePolicyRepository.findById(request.attendancePolicyId())
                .orElseThrow(() -> new AttendancePolicyNotFoundException("Attendance Policy not found"));

        Shift shift = shiftRepository.findById(request.shiftId())
                .orElseThrow(() -> new ShiftNotFoundException("Shift not found"));


        if (request.workDate() != null) attendanceSummary.setWorkDate(request.workDate());
        if (request.shiftId() != null) attendanceSummary.setShiftId(request.shiftId());
        if (request.attendancePolicyId() != null) attendanceSummary.setAttendancePolicyId(request.attendancePolicyId());
        attendanceSummary.setInTime(request.inTime() != null ? request.inTime() : attendanceSummary.getInTime());
        attendanceSummary.setOutTime(request.outTime() != null ? request.outTime() : attendanceSummary.getOutTime());
        attendanceSummary.setTotalWorkMinutes(request.totalWorkMinutes() != null ? request.totalWorkMinutes() : attendanceSummary.getTotalWorkMinutes());
        attendanceSummary.setLateMinutes(request.lateMinutes() != null ? request.lateMinutes() : attendanceSummary.getLateMinutes());
        attendanceSummary.setEarlyLeaveMinutes(request.earlyLeaveMinutes() != null ? request.earlyLeaveMinutes() : attendanceSummary.getEarlyLeaveMinutes());
        attendanceSummary.setOvertimeMinutes(request.overtimeMinutes() != null ? request.overtimeMinutes() : attendanceSummary.getOvertimeMinutes());
        attendanceSummary.setAttendanceType(request.attendanceType() != null ? request.attendanceType() : attendanceSummary.getAttendanceType());
        attendanceSummary.setDayStatus(request.dayStatus() != null ? request.dayStatus() : attendanceSummary.getDayStatus());
        attendanceSummary.setManualRequestId(request.manualRequestId() != null ? request.manualRequestId() : attendanceSummary.getManualRequestId());
        attendanceSummary.setRemarks(request.remarks() != null ? request.remarks() : attendanceSummary.getRemarks());
        attendanceSummary.setComputedAt(request.computedAt() != null ? request.computedAt() : attendanceSummary.getComputedAt());
        attendanceSummary.setIsLocked(request.isLocked() != null ? request.isLocked() : attendanceSummary.getIsLocked());

        // 4 updated Shift
        AttendanceSummary savedAttendanceSummary = attendanceSummaryRepository.save(attendanceSummary);

        // 5 return DTO
        return attendanceSummaryMapper.toDTO(savedAttendanceSummary);
    }

    @Override
    public void destroy(Long attendanceSummaryId) {
        AttendanceSummary attendanceSummary = attendanceSummaryRepository.findById(attendanceSummaryId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        attendanceSummaryRepository.delete(attendanceSummary);
    }

    @Override
    public AttendanceSummaryDTO createInitialAttendanceSummary(ManualAttendance manualAttendance, Long empSerialId) {

        ManualAttendance manual = manualAttendanceRepository.findById(manualAttendance.getId())
                .orElseThrow(() -> new AttendancePolicyNotFoundException("Manual attendance not found " + manualAttendance.getId()));

        List<AttendancePolicy> policies = attendancePolicyRepository.findAll();
        AttendancePolicy attendancePolicy = policies.isEmpty() ? null : policies.get(0);

        Employee employee = employeeRepository.findById(manualAttendance.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFound("Employee not found"));

        if (employee.getShift() == null) {
            throw new ShiftNotFoundException("Shift not assigned to employee with ID: " + employee.getId());
        }

        Shift shift = shiftRepository.findById(employee.getShift().getId())
                .orElseThrow(() -> new ShiftNotFoundException("Shift not found"));

        AttendanceSummary attendanceSummary = new AttendanceSummary();
        attendanceSummary.setCompanyId(employee.getCompany().getId());
        attendanceSummary.setEmployeeId(manual.getEmployeeId());
        attendanceSummary.setWorkDate(manual.getAttendanceDate());
        attendanceSummary.setShiftId(shift.getId());
        attendanceSummary.setInTime(manual.getInTime());
        attendanceSummary.setOutTime(manual.getOutTime());
        attendanceSummary.setAttendancePolicyId(attendancePolicy.getId());
        attendanceSummary.setManualRequestId(manual.getId());
        attendanceSummary.setEmployeeSerialId(empSerialId);

        attendanceSummary.setTotalWorkMinutes(
                attendanceUtil.calculateTotalWorkMinutes(manual.getInTime(), manual.getOutTime())
        );

        System.err.println("total worktime: " + attendanceSummary.getTotalWorkMinutes());

        attendanceSummary.setLateMinutes(
                attendanceUtil.calculateLateMinutes(manual, shift, attendancePolicy)
        );

        System.err.println("late time : " + attendanceSummary.getLateMinutes());

        attendanceSummary.setEarlyLeaveMinutes(
                attendanceUtil.calculateEarlyLeaveMinutes(manual, shift, attendancePolicy)
        );

        System.err.println("early leave time : " + attendanceSummary.getEarlyLeaveMinutes());

        attendanceSummary.setOvertimeMinutes(
                attendanceUtil.calculateOvertimeMinutes(manual.getOutTime(), shift)
        );

        System.err.println("over time : " + attendanceSummary.getOvertimeMinutes());

        attendanceSummary.setDayStatus(
                attendanceUtil.determineDayStatus(
                        manual.getInTime() != null ? manual.getInTime().toLocalTime() : null,
                        manual.getOutTime() != null ? manual.getOutTime().toLocalTime() : null,
                        attendancePolicy,
                        shift
                )
        );

        System.err.println("over time : " + attendanceSummary.getDayStatus());

        attendanceSummary.setAttendanceType(MANUAL);
        attendanceSummary.setIsLocked(manual.getIsLocked());
        attendanceSummary.setComputedAt(LocalDateTime.now());

        // Save and return as DTO
        AttendanceSummary saved = attendanceSummaryRepository.save(attendanceSummary);

        return attendanceSummaryMapper.toDTO(saved);
    }

    @Override
    public List<DeviceTypeDTO> getRecentAttendance(Long employeeId, Integer limit) {
        int finalLimit = (limit != null && limit > 0) ? limit : 5;
        return attendanceSummaryRepository.findRecentAttendanceByEmployee(employeeId, finalLimit);
    }

    @Override
    public List<AttendanceStatusDTO> getAttendanceStatus() {

        List<AttendanceStatusDTO> attendanceStatusDTOs = new ArrayList<>();
        int targetYear = Year.now().getValue();

        if (AuthUtils.getCurrentUsername() == null || AuthUtils.getCurrentUsername().isEmpty())
            throw new UsernameNotFoundException("User not found");
        String identifier = AuthUtils.getCurrentUsername();
        User  user = userRepository.findByEmailOrPhone(identifier, identifier)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email/phone: " + identifier));

        if(user.getEmployeeSerialId()==null || user.getEmployeeSerialId()==0)
            throw new UsernameNotFoundException("Employee serial not found");

         Employee employee = employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .filter(p -> p.getEmployeeSerialId().equals(user.getEmployeeSerialId().longValue())).findFirst().orElseThrow(() -> new EmployeeNotFound("Employee not found"));

        List<AttendanceSummary> attendanceList = attendanceSummaryRepository
                .findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .filter(p -> p.getEmployeeId().equals(employee.getId()))
                .filter(p -> p.getWorkDate() != null && p.getWorkDate().getYear() == targetYear)
                .toList();

         if(attendanceList.isEmpty())
             throw new AttendanceSummaryNotFoundException("Attendance summary data not found " + employee.getId());

         attendanceList.stream().forEach(p->{
             AttendanceStatusDTO dto = new AttendanceStatusDTO();
             dto.setStatus(p.getDayStatus().name());
             dto.setColorCode(null);
             dto.setStatusDate(p.getWorkDate());
             attendanceStatusDTOs.add(dto);
         });

        return attendanceStatusDTOs;
    }
}
