package com.betopia.hrm.services.attendance.attendanceapproval.impl;

import com.betopia.hrm.domain.attendance.entity.AttendanceApproval;
import com.betopia.hrm.domain.attendance.entity.AttendanceSummary;
import com.betopia.hrm.domain.attendance.entity.ManualAttendance;
import com.betopia.hrm.domain.attendance.exception.AttendanceApprovalException;
import com.betopia.hrm.domain.attendance.exception.AttendancePolicyNotFoundException;
import com.betopia.hrm.domain.attendance.repository.AttendanceApprovalRepository;
import com.betopia.hrm.domain.attendance.repository.ManualAttendanceRepository;
import com.betopia.hrm.domain.attendance.request.ApproveAttendanceStatusRequest;
import com.betopia.hrm.domain.attendance.request.AttendanceApprovalRequest;
import com.betopia.hrm.domain.attendance.specification.AttendanceSpecification;
import com.betopia.hrm.domain.attendance.specification.ManualApprovalSpecification;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.AttendanceApprovalDTO;
import com.betopia.hrm.domain.dto.attendance.mapper.AttendanceApprovalMapper;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.attendance.attendanceapproval.AttendanceApprovalService;
import com.betopia.hrm.webapp.util.AttendanceUtil;
import com.betopia.hrm.webapp.util.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class AttendanceApprovalServiceImpl implements AttendanceApprovalService {

    private final AttendanceApprovalRepository attendanceApprovalRepository;
    private final EmployeeRepository employeeRepository;
    private final AttendanceApprovalMapper attendanceApprovalMapper;
    private final ManualAttendanceRepository manualAttendanceRepository;
    private final AttendanceUtil attendanceUtil;
    private final UserRepository userRepository;


    public AttendanceApprovalServiceImpl(AttendanceApprovalRepository attendanceApprovalRepository, EmployeeRepository employeeRepository,
                                         AttendanceApprovalMapper attendanceApprovalMapper,
                                         ManualAttendanceRepository manualAttendanceRepository,
                                         AttendanceUtil attendanceUtil, UserRepository userRepository) {
        this.attendanceApprovalRepository = attendanceApprovalRepository;
        this.employeeRepository = employeeRepository;
        this.attendanceApprovalMapper = attendanceApprovalMapper;
        this.manualAttendanceRepository = manualAttendanceRepository;
        this.attendanceUtil = attendanceUtil;
        this.userRepository = userRepository;
    }

    @Override
    public PaginationResponse<AttendanceApprovalDTO> index(Sort.Direction direction, int page, int perPage, String keyword) {

        Specification<AttendanceApproval> spec =
                ManualApprovalSpecification.globalSearch(keyword);

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<AttendanceApproval> AttendanceApprovalPage = attendanceApprovalRepository.findAll(spec,pageable);
        List<AttendanceApproval> AttendanceApprovalDTO = AttendanceApprovalPage.getContent();
        List<AttendanceApprovalDTO> AttendanceApprovalDTOs = attendanceApprovalMapper.toDTOList(AttendanceApprovalDTO);
        PaginationResponse<AttendanceApprovalDTO> response = new PaginationResponse<>();
        response.setData(AttendanceApprovalDTOs);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All Attendance Summary fetched successfully");

        Links links = Links.fromPage(AttendanceApprovalPage, "/attendance-approval");
        response.setLinks(links);

        Meta meta = Meta.fromPage(AttendanceApprovalPage, "/attendance-approval");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<AttendanceApprovalDTO> getAllAttendanceApprovals() {
        List<AttendanceApproval> attendanceApprovals = attendanceApprovalRepository.findAll();
        return attendanceApprovalMapper.toDTOList(attendanceApprovals);
    }


    public AttendanceApprovalDTO store(AttendanceApprovalRequest request) {


        return null;
    }

    @Override
    public AttendanceApprovalDTO show(Long attendanceApprovalId) {
        AttendanceApproval attendanceApproval = attendanceApprovalRepository.findById(attendanceApprovalId)
                .orElseThrow(() -> new RuntimeException("Attendance approval  not found with id: " + attendanceApprovalId));
        return attendanceApprovalMapper.toDTO(attendanceApproval);
    }


    public AttendanceApprovalDTO update(Long attendanceApprovalId, AttendanceApprovalRequest request) {
        return null;
    }

    public void destroy(Long attendanceApprovalId) {
        AttendanceApproval attendanceApproval = attendanceApprovalRepository.findById(attendanceApprovalId)
                .orElseThrow(() -> new RuntimeException("Approval not found"));

        attendanceApprovalRepository.delete(attendanceApproval);
    }

    @Override
    public AttendanceApprovalDTO createInitialAttendanceApproval(ManualAttendance manualAttendance) {
        Employee employee = employeeRepository.findById(manualAttendance.getEmployeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + manualAttendance.getEmployeeId()));

        ManualAttendance manual = manualAttendanceRepository.findById(manualAttendance.getId())
                .orElseThrow(() -> new AttendancePolicyNotFoundException("Manual attendance not found " + manualAttendance.getId()));

        Employee supervisor = employeeRepository.findById(employee.getSupervisorId())
                .orElseThrow(() -> new RuntimeException("Employee supervisor not found with id: " + employee.getId()));

        User approverUser = userRepository.findByEmployeeSerialId(Math.toIntExact(supervisor.getEmployeeSerialId()))
                .orElseThrow(() -> new RuntimeException("User not found with id: " + supervisor.getEmployeeSerialId()));

        AttendanceApproval attendanceApproval = new AttendanceApproval();
        attendanceApproval.setEmployeeId(manual.getEmployeeId());
        attendanceApproval.setEmployeeName(attendanceUtil.getFullName(employee));
        attendanceApproval.setEmployeeSerialId(employee.getEmployeeSerialId());
        attendanceApproval.setManualAttendanceId(manual.getId());
        attendanceApproval.setDate(manual.getAttendanceDate());
        attendanceApproval.setInTime(manual.getInTime());
        attendanceApproval.setOutTime(manual.getOutTime());
        attendanceApproval.setManualApprovalStatus(manual.getManualAttendanceStatus());
        attendanceApproval.setAdjustmentType(manual.getAdjustmentType());
        attendanceApproval.setReason(manual.getReason());
        attendanceApproval.setSubmittedBy(manual.getSourceChannel());
        attendanceApproval.setSubmittedAt(manual.getSubmittedAt());
        if (approverUser != null){
            attendanceApproval.setApproverId(approverUser.getId());
            attendanceApproval.setCreatedBy(approverUser.getId());
        }

        else
            throw new UsernameNotFoundException("User not found");
        attendanceApproval.setCreatedDate(LocalDateTime.now());


        AttendanceApproval saved = attendanceApprovalRepository.save(attendanceApproval);
        return attendanceApprovalMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public List<AttendanceApprovalDTO> updateAttendanceStatus(List<ApproveAttendanceStatusRequest> requests) {
        List<AttendanceApprovalDTO> result = new ArrayList<>();

        for (ApproveAttendanceStatusRequest request : requests) {

            // Step 1: Fetch existing attendance approval
            AttendanceApproval attendanceApproval = attendanceApprovalRepository.findById(request.id())
                    .orElseThrow(() -> new AttendanceApprovalException("Approval not found with id: " + request.id()));

            // Step 2: Update approval info
            attendanceApproval.setManualApprovalStatus(request.manualApprovalStatus());
            attendanceApproval.setReason(request.reason());
            AttendanceApproval savedApproval = attendanceApprovalRepository.save(attendanceApproval);

            // Step 3: Update related manual attendance record
            ManualAttendance manual = manualAttendanceRepository.findById(attendanceApproval.getManualAttendanceId())
                    .orElseThrow(() -> new AttendancePolicyNotFoundException(
                            "Manual attendance not found with id: " + attendanceApproval.getManualAttendanceId()));

            manual.setManualAttendanceStatus(request.manualApprovalStatus());
            manualAttendanceRepository.save(manual);

            // Step 4: Map and add to result
            result.add(attendanceApprovalMapper.toDTO(savedApproval));
        }

        return result;
    }


    @Override
    public List<AttendanceApprovalDTO> getApprovalsForSupervisor() {
        User users = null;
        String identifier = AuthUtils.getCurrentUsername();

        if (identifier != null && !identifier.isEmpty()) {
            users = userRepository.findByEmailOrPhone(identifier, identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email/phone: " + identifier));
        } else {
            throw new EntityNotFoundException("No authenticated user found for submission.");
        }

//        List<User> submiitedby = userRepository.findAll();
//        User users = submiitedby.isEmpty() ? null : submiitedby.get(0);

        List<AttendanceApproval> approvals = attendanceApprovalRepository.findByApproverId(users.getId());

        if (approvals.isEmpty()) {
            throw new EntityNotFoundException("No attendance approvals found for this user " + users.getId());
        }

        return approvals.stream()
                .map(attendanceApprovalMapper::toDTO)
                .collect(Collectors.toList());
    }
}
