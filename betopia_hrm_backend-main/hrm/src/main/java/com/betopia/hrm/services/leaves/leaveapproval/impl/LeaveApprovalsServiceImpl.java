package com.betopia.hrm.services.leaves.leaveapproval.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeaveApprovalsDTO;
import com.betopia.hrm.domain.dto.leave.mapper.LeaveApprovalsMapper;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.enums.LeaveStatus;
import com.betopia.hrm.domain.leave.repository.LeaveApprovalsRepository;
import com.betopia.hrm.domain.leave.repository.LeaveRequestRepository;
import com.betopia.hrm.domain.leave.request.LeaveApprovalsRequest;
import com.betopia.hrm.domain.leave.request.StatusApproveRequest;
import com.betopia.hrm.domain.leave.specification.LeaveApplySpecification;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.domain.users.specification.UserSpecification;
import com.betopia.hrm.services.leaves.leaveapproval.LeaveApprovalsService;
import com.betopia.hrm.services.leaves.leavebalanceemployee.LeaveBalanceEmployeeService;
import com.betopia.hrm.services.leaves.leavenotifications.LeaveNotificationsService;
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
public class LeaveApprovalsServiceImpl implements LeaveApprovalsService {

    public final LeaveApprovalsRepository leaveApprovalsRepository;
    public final LeaveApprovalsMapper leaveApprovalsMapper;
    private final EmployeeRepository employeeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveBalanceEmployeeService balanceEmployeeService;
    private final LeaveNotificationsService leaveNotificationsService;
    private final UserRepository userRepository;
    private final AttendanceUtil attendanceUtil;

    public LeaveApprovalsServiceImpl(LeaveApprovalsRepository leaveApprovalsRepository,
                                     LeaveApprovalsMapper leaveApprovalsMapper, EmployeeRepository employeeRepository,
                                     LeaveRequestRepository leaveRequestRepository,
                                     LeaveBalanceEmployeeService balanceEmployeeService,
                                     LeaveNotificationsService leaveNotificationsService, UserRepository userRepository,
                                     AttendanceUtil attendanceUtil) {

        this.leaveApprovalsMapper = leaveApprovalsMapper;
        this.leaveApprovalsRepository = leaveApprovalsRepository;
        this.employeeRepository = employeeRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.balanceEmployeeService = balanceEmployeeService;
        this.leaveNotificationsService = leaveNotificationsService;
        this.userRepository = userRepository;
        this.attendanceUtil = attendanceUtil;
    }

    @Override
    public PaginationResponse<LeaveApprovalsDTO> index(Sort.Direction direction, int page, int perPage,String keyword) {
        Specification<LeaveApprovals> spec =
                LeaveApplySpecification.globalSearch(keyword);

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<LeaveApprovals> approvalsPage = leaveApprovalsRepository.findAll(spec,pageable);
        List<LeaveApprovalsDTO> leaveApprovalsDTOS = approvalsPage.getContent()
                .stream()
                .map(leaveApprovalsMapper::toDTO)
                .toList();
        PaginationResponse<LeaveApprovalsDTO> response = new PaginationResponse<>();
        response.setData(leaveApprovalsDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All leave approval fetched successfully");

        Links links = Links.fromPage(approvalsPage, "/leave-approval");
        response.setLinks(links);

        Meta meta = Meta.fromPage(approvalsPage, "/leave-approval");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<LeaveApprovalsDTO> getAll() {
        List<LeaveApprovals> leaveApprovals = leaveApprovalsRepository.findAll();
        return leaveApprovalsMapper.toDTOList(leaveApprovals);
    }

    @Override
    public LeaveApprovalsDTO store(LeaveApprovalsRequest request) {
        LeaveApprovals leaveApprovals = new LeaveApprovals();

        if (AuthUtils.getCurrentUsername() == null)
            throw new UsernameNotFoundException("User not found");

        User user = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .filter(p -> p.getEmail().equals(AuthUtils.getCurrentUsername()) || p.getPhone().equals(AuthUtils.getCurrentUsername()))
                .findFirst().orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getEmployeeSerialId() == null || user.getEmployeeSerialId() == 0)
            throw new UsernameNotFoundException("Employee serial not found");

        Employee employee = employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .filter(p -> p.getEmployeeSerialId().equals(user.getEmployeeSerialId().longValue())).findFirst().orElseThrow(() -> new EmployeeNotFound("Employee not found"));

        User approverUser = userRepository.findByEmployeeSerialId(Math.toIntExact(employee.getSupervisorId()))
                .orElse(null);
        // Get supervisor entity

        leaveApprovals.setApproverId(approverUser == null ? null : approverUser.getId());

        leaveApprovals.setLeaveRequest(leaveRequestRepository.findById(request.leaveRequestId())
                .orElseThrow(() -> new RuntimeException("Leave request id not found")));


        leaveApprovals.setEmployee(employee);
        leaveApprovals.setLevel(request.level());
        leaveApprovals.setLeaveStatus(request.leaveStatus());
        leaveApprovals.setRemarks(request.remarks());
      /*  leaveApprovals.setEmployeeName(attendanceUtil.getFullName(employee));
        leaveApprovals.setEmployeeSerialId(employee.getEmployeeSerialId());*/
        leaveApprovals.setActionDate(request.actionDate());


        // Save entity
        LeaveApprovals save = leaveApprovalsRepository.save(leaveApprovals);

        LeaveRequest leaveRequest = leaveRequestRepository.findById(
                leaveApprovals.getLeaveRequest().getId()
        ).orElseThrow(() -> new RuntimeException("LeaveRequest not found with id: " + leaveApprovals.getLeaveRequest().getId()));

        // Step 3: Update the related leave request status
        leaveRequest.setStatus(request.leaveStatus());
        leaveRequestRepository.save(leaveRequest);

        // Step 4: If approved, deduct leave from balance
        if (request.leaveStatus() == LeaveStatus.APPROVED) {
            balanceEmployeeService.deductLeaveBalance(leaveRequest);
        }

        // Convert Entity to DTO and return
        return leaveApprovalsMapper.toDTO(save);
    }

    @Override
    public LeaveApprovalsDTO show(Long id) {
        LeaveApprovals leaveApprovals = leaveApprovalsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("approval not found with id: " + id));
        return leaveApprovalsMapper.toDTO(leaveApprovals);
    }

    @Override
    public LeaveApprovalsDTO update(Long id, LeaveApprovalsRequest request) {
        LeaveApprovals leaveApprovals = leaveApprovalsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave approve not found with id: " + id));

        if (AuthUtils.getCurrentUsername() == null)
            throw new UsernameNotFoundException("User not found");

        User user = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .filter(p -> p.getEmail().equals(AuthUtils.getCurrentUsername()) || p.getPhone().equals(AuthUtils.getCurrentUsername()))
                .findFirst().orElseThrow(() -> new UsernameNotFoundException("User not found"));

        if (user.getEmployeeSerialId() == null || user.getEmployeeSerialId() == 0)
            throw new UsernameNotFoundException("Employee serial not found");

        Employee employee = employeeRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
                .filter(p -> p.getEmployeeSerialId().equals(user.getEmployeeSerialId().longValue())).findFirst().orElseThrow(() -> new EmployeeNotFound("Employee not found"));

        // Get supervisor entity
        User approverUser = userRepository.findByEmployeeSerialId(Math.toIntExact(employee.getSupervisorId()))
                .orElse(null);

        leaveApprovals.setApproverId(approverUser == null ? null : approverUser.getId());

        leaveApprovals.setLeaveRequest(leaveRequestRepository.findById(request.leaveRequestId())
                .orElseThrow(() -> new RuntimeException("Leave request id not found")));

        leaveApprovals.setEmployee(employee);
        leaveApprovals.setLevel(request.level());
        leaveApprovals.setLeaveStatus(request.leaveStatus());
        leaveApprovals.setRemarks(request.remarks());

        leaveApprovals.setActionDate(request.actionDate());


        // Save entity
        LeaveApprovals update = leaveApprovalsRepository.save(leaveApprovals);

        // Convert Entity to DTO and return
        return leaveApprovalsMapper.toDTO(update);
    }

    @Override
    public void destroy(Long id) {

        LeaveApprovals leaveApprovals = leaveApprovalsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("approval not found"));

        leaveApprovalsRepository.delete(leaveApprovals);

    }

    @Override
    public LeaveApprovalsDTO createInitialApproval(LeaveRequest request) {
        // Get the employee who requested the leave
        Employee employee = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.getEmployeeId()));

        // Get supervisor entity
        Long supervisorId = employee.getSupervisorId();
        System.err.println("employee " + request.getEmployeeId());
        System.err.println("supervisor " + supervisorId);
        if (supervisorId == null) {
            throw new IllegalStateException("Employee " + employee.getId() + " does not have a supervisor assigned");
        }

        Employee supervisor = employeeRepository.findById(employee.getSupervisorId())
                .orElseThrow(() -> new RuntimeException("Employee supervisor not found with id: " + request.getEmployeeId()));

        User approverUser = userRepository.findByEmployeeSerialId(Math.toIntExact(supervisor.getEmployeeSerialId()))
                .orElseThrow(() -> new RuntimeException("User not found with id: " + supervisor.getEmployeeSerialId()));

        LeaveRequest leaveRequest = leaveRequestRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + request.getId()));

        // Create leave approval entity
        LeaveApprovals approval = new LeaveApprovals();
        approval.setEmployee(employee);
        approval.setLeaveRequest(leaveRequest); // set leave request entity
        approval.setApproverId(approverUser == null ? null : approverUser.getId());       // set supervisor entity
        approval.setLevel(1);                    // first approval level
        approval.setLeaveStatus(request.getStatus());

        // Save and return as DTO
        LeaveApprovals saved = leaveApprovalsRepository.save(approval);

        leaveNotificationsService.createInitialNotification(saved);

        return leaveApprovalsMapper.toDTO(saved);
    }

    @Transactional
    @Override
    public List<LeaveApprovalsDTO> updateLeaveStatus(List<StatusApproveRequest> requests) {
        List<LeaveApprovalsDTO> result = new ArrayList<>();

        for (StatusApproveRequest request : requests) {

            // Step 1: Get existing approval entry
            LeaveApprovals approval = leaveApprovalsRepository.findById(request.id())
                    .orElseThrow(() -> new RuntimeException("Leave approval not found with id: " + request.id()));

            if (approval.getLeaveStatus() == LeaveStatus.APPROVED) {
                throw new RuntimeException("Leave request ID " + request.id() + " is already approved and cannot be modified.");
            }

            if (request.leaveStatus() == LeaveStatus.REJECTED &&
                    (request.remarks() == null || request.remarks().trim().isEmpty())) {
                throw new RuntimeException("Remarks are mandatory when rejecting a leave request (ID: " + request.id() + ").");
            }

            // Step 2: Update approval info
            approval.setLeaveStatus(request.leaveStatus());
            approval.setRemarks(request.remarks());
            approval.setActionDate(LocalDateTime.now());

            LeaveApprovals savedApproval = leaveApprovalsRepository.save(approval);

            // Step 3: Update related LeaveRequest
            LeaveRequest leaveRequest = leaveRequestRepository.findById(
                    approval.getLeaveRequest().getId()
            ).orElseThrow(() -> new RuntimeException("LeaveRequest not found with id: " + approval.getLeaveRequest().getId()));

            System.err.println("Before update: LeaveRequest ID=" + leaveRequest.getId() +
                    ", Status=" + leaveRequest.getStatus() +
                    ", Version=" + leaveRequest.getVersion());

            leaveRequest.setStatus(request.leaveStatus());
            leaveRequestRepository.save(leaveRequest);

            System.err.println("After update: LeaveRequest ID=" + leaveRequest.getId() +
                    ", Status=" + leaveRequest.getStatus() +
                    ", Version=" + leaveRequest.getVersion());

            // Step 4: If approved, deduct leave balance
            if (request.leaveStatus() == LeaveStatus.APPROVED) {
                balanceEmployeeService.deductLeaveBalance(leaveRequest);
            }

            // Step 5: Add to result
            result.add(leaveApprovalsMapper.toDTO(savedApproval));
        }

        return result;
    }

    @Override
    public List<LeaveApprovalsDTO> getApprovalsForSupervisor() {

        User users = null;
        String identifier = AuthUtils.getCurrentUsername();

        if (identifier != null && !identifier.isEmpty()) {
            users = userRepository.findByEmailOrPhone(identifier, identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email/phone: " + identifier));
        } else {
            throw new EntityNotFoundException("No authenticated user found for submission.");
        }

//        List<User> submiitedby = userRepository.findAll();
//        User u = submiitedby.isEmpty() ? null : submiitedby.get(0);


        List<LeaveApprovals> approvals = leaveApprovalsRepository.findByApproverId(users.getId());

        if (approvals.isEmpty()) {
            throw new EntityNotFoundException("No leave approvals found for this user " + users.getId());
        }

        return approvals.stream()
                .map(leaveApprovalsMapper::toDTO)
                .collect(Collectors.toList());
    }
}


