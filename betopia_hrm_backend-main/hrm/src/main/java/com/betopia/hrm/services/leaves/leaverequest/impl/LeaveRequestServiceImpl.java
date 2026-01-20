package com.betopia.hrm.services.leaves.leaverequest.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeaveRequestDTO;
import com.betopia.hrm.domain.dto.leave.mapper.LeaveRequestMapper;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import com.betopia.hrm.domain.leave.entity.LeaveGroupAssign;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.entity.LeaveRequestDocument;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.leave.enums.LeaveStatus;
import com.betopia.hrm.domain.leave.mapper.ManualLeaveRequestMapper;
import com.betopia.hrm.domain.leave.repository.LeaveGroupAssignRepository;
import com.betopia.hrm.domain.leave.repository.LeaveRequestRepository;
import com.betopia.hrm.domain.leave.repository.LeaveTypeRepository;
import com.betopia.hrm.domain.leave.request.LeaveRequestCreateRequest;
import com.betopia.hrm.domain.leave.specification.LeaveApplySpecification;
import com.betopia.hrm.domain.leave.specification.LeaveRequestSpecification;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.leaves.leaveapproval.LeaveApprovalsService;
import com.betopia.hrm.services.leaves.leavebalanceemployee.LeaveBalanceEmployeeService;
import com.betopia.hrm.services.leaves.leaverequest.LeaveRequestService;
import com.betopia.hrm.webapp.util.AuthUtils;
import com.betopia.hrm.webapp.util.NotificationSender;
import com.betopia.hrm.webapp.util.S3Service;
import com.betopia.hrm.webapp.util.SmsResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class LeaveRequestServiceImpl implements LeaveRequestService {

    private final LeaveRequestRepository repository;
    private final ManualLeaveRequestMapper mapper;
    private final LeaveBalanceEmployeeService balanceEmployeeService;
    private final NotificationSender notificationSender;
    private final EmployeeRepository employeeRepository;
    private final TemplateEngine templateEngine;
    private final S3Service s3Service;
    private final LeaveRequestMapper leaveRequestMapper;
    private final LeaveApprovalsService leaveApprovalsService;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveGroupAssignRepository leaveGroupAssignRepository;
    private final UserRepository userRepository;


    public LeaveRequestServiceImpl(
            LeaveRequestRepository repository,
            ManualLeaveRequestMapper mapper,
            LeaveBalanceEmployeeService balanceEmployeeService,
            NotificationSender notificationSender,
            EmployeeRepository employeeRepository,
            TemplateEngine templateEngine, S3Service s3Service,
            LeaveRequestMapper leaveRequestMapper,
            LeaveApprovalsService leaveApprovalsService,
            LeaveTypeRepository leaveTypeRepository,
            LeaveGroupAssignRepository leaveGroupAssignRepository, UserRepository userRepository
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.balanceEmployeeService = balanceEmployeeService;
        this.notificationSender = notificationSender;
        this.employeeRepository = employeeRepository;
        this.templateEngine = templateEngine;
        this.s3Service = s3Service;
        this.leaveRequestMapper = leaveRequestMapper;
        this.leaveApprovalsService = leaveApprovalsService;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveGroupAssignRepository = leaveGroupAssignRepository;
        this.userRepository = userRepository;
    }


    @Override
    public PaginationResponse<LeaveRequestDTO> index(Sort.Direction direction, int page, int perPage, Long userId,String keyword) {

        Specification<LeaveRequest> spec =
                LeaveRequestSpecification.globalSearch(keyword);

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<LeaveRequest> leaveRequestPage = repository.findAll(spec,pageable);

        List<LeaveRequestDTO> leaveRequests = leaveRequestPage.getContent()
                .stream()
                .filter(lr -> userId == null || lr.getCreatedBy().equals(userId)) // filter if userId is provided
                .map(leaveRequestMapper::toDTO)
                .toList();

        PaginationResponse<LeaveRequestDTO> response = new PaginationResponse<>();
        response.setData(leaveRequests);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All leave requests fetched successfully");

        Links links = Links.fromPage(leaveRequestPage, "/leave-requests");
        response.setLinks(links);

        Meta meta = Meta.fromPage(leaveRequestPage, "/leave-requests");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<LeaveRequestDTO> getAllLeaveRequests(Long userId) {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "id")) // fetch all
                .stream()
                .filter(lr -> userId == null || lr.getCreatedBy().equals(userId)) // filter if userId is provided
                .map(leaveRequestMapper::toDTO)
                .toList();
    }

    @Override
    public LeaveRequest getLeaveRequestById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));
    }

    @Override
    public LeaveRequestDTO store(LeaveRequestCreateRequest request, List<MultipartFile> files) {
        LeaveRequest entity = mapper.toEntity(request);

        if (AuthUtils.getCurrentUsername() == null)
            throw new UsernameNotFoundException("User not found");

        User user = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .filter(p -> p.getEmail().equals(AuthUtils.getCurrentUsername()) || p.getPhone().equals(AuthUtils.getCurrentUsername()))
                .findFirst().orElseThrow(() -> new UsernameNotFoundException("User not found"));

        entity.setCreatedBy(user.getId());
        entity.setCreatedDate(LocalDateTime.now());
        if (files != null && !files.isEmpty()) {
            System.err.println("found the file");
            List<LeaveRequestDocument> proofDocuments = saveDocuments(files, entity);
            entity.setProofDocumentPath(proofDocuments);
        } else {
            System.err.println("not found the file");
            entity.setProofDocumentPath(Collections.emptyList());
        }

        balanceEmployeeService.applyLeave(entity);

        LeaveRequest savedRequest = repository.save(entity);
        leaveApprovalsService.createInitialApproval(savedRequest);
        return leaveRequestMapper.toDTO(savedRequest);

    }

    @Override
    public LeaveRequest updateLeaveRequest(Long id, LeaveRequestCreateRequest request) {
        LeaveRequest entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));
        if (AuthUtils.getCurrentUsername() == null)
            throw new UsernameNotFoundException("User not found");

        User user = userRepository.findAll(Sort.by(Sort.Direction.ASC, "id")).stream()
                .filter(p -> p.getEmail().equals(AuthUtils.getCurrentUsername()) || p.getPhone().equals(AuthUtils.getCurrentUsername()))
                .findFirst().orElseThrow(() -> new UsernameNotFoundException("User not found"));

        mapper.updateEntity(entity, request);
        entity.setLastModifiedBy(user.getId());
        entity.setLastModifiedDate(LocalDateTime.now());

        return repository.save(entity);
    }

    @Override
    public void deleteLeaveRequest(Long id) {
        LeaveRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));

        repository.delete(request);
    }

    @Override
    public List<LeaveRequest> findByEmployeeId(Long employeeId) {
        return repository.findAll().stream()
                .filter(request -> request.getEmployeeId() != null &&
                        request.getEmployeeId().equals(employeeId))
                .collect(Collectors.toList());
    }

    @Override
    public SmsResponse sendLeaveApprovalNotification(Long leaveRequestId) {
        LeaveRequest leave = repository.findById(leaveRequestId)
                .orElseThrow(() -> new RuntimeException("LeaveRequest not found"));

        if (leave.getStatus() != null
                && leave.getStatus().equals(LeaveStatus.APPROVED)
                || leave.getStatus().equals(LeaveStatus.REJECTED)) {
            Employee employee = employeeRepository.findById(leave.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            // Prepare Thymeleaf context
            Context context = new Context();
            context.setVariable("status",leave.getStatus());
            context.setVariable("employeeName", employee.getFirstName());
            context.setVariable("leaveType", leave.getLeaveType().getName());
            context.setVariable("startDate", leave.getStartDate());
            context.setVariable("endDate", leave.getEndDate());

            // Process template
            String msg = templateEngine.process("leavetemplate.txt", context);

            // Send SMS via generic NotificationSender
            SmsResponse response = notificationSender.sendSms(employee.getPhone(), msg);
            response.setEmployeeId(employee.getId().toString());
            return response;
        }

        throw new RuntimeException("Leave not approved yet");
    }

    private List<LeaveRequestDocument> saveDocuments(List<MultipartFile> files, LeaveRequest leaveRequest) {
        if (files == null || files.isEmpty()) {
            return Collections.emptyList();
        }

        return files.stream()
                .map(file->createLeaveRequestDocument(file, leaveRequest))
                .toList();
    }

    private LeaveRequestDocument createLeaveRequestDocument(MultipartFile file, LeaveRequest leaveRequest) {
        try {
            // Convert MultipartFile → Temp File
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            file.transferTo(convFile);

            // Build S3 key (use UUID to avoid name collisions)
            String imageKey = "leaveRequest/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Upload to S3
            String imageUrl = s3Service.uploadFile(imageKey, convFile);

            // Create entity (without Lombok)
            LeaveRequestDocument doc = new LeaveRequestDocument();
            doc.setFileName(imageKey);
            doc.setFileType(file.getContentType());
            doc.setFilePath(imageUrl);
            doc.setLeaveRequest(leaveRequest);

            return doc;

        } catch (IOException e) {
            throw new RuntimeException("File saving failed: " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public LeaveRequestDTO update(Long id, LeaveRequestCreateRequest request, List<MultipartFile> files) {
        LeaveRequest leaveRequest = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave Request not found with id: " + id));

        // Update basic fields
        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + request.employeeId()));

        LeaveType leaveType = leaveTypeRepository.findById(request.leaveTypeId())
                .orElseThrow(() -> new EntityNotFoundException("LeaveType not found with id: " + request.leaveTypeId()));

        LeaveGroupAssign leaveGroupAssign = leaveGroupAssignRepository.findById(request.leaveGroupAssignId())
                .orElseThrow(() -> new EntityNotFoundException("LeaveGroupAssign not found with id: " + request.leaveGroupAssignId()));

        leaveRequest.setEmployeeId(employee.getId());
        leaveRequest.setLeaveType(leaveType);
        leaveRequest.setLeaveGroupAssign(leaveGroupAssign);
        leaveRequest.setStartDate(request.startDate());
        leaveRequest.setEndDate(request.endDate());
        leaveRequest.setDaysRequested(request.daysRequested());
        leaveRequest.setReason(request.reason());
        leaveRequest.setJustification(request.justification());
        leaveRequest.setRequestedAt(LocalDateTime.now());
        leaveRequest.setStatus(request.status());

        // Handle uploaded files
        if (files != null && !files.isEmpty()) {
            List<LeaveRequestDocument> proofDocuments = saveDocuments(files, leaveRequest);

            // Add existing documents if any
            if (leaveRequest.getProofDocumentPath() != null) {
                proofDocuments.addAll(leaveRequest.getProofDocumentPath());
            }

            leaveRequest.setProofDocumentPath(proofDocuments);
        }

        LeaveRequest saved = repository.save(leaveRequest);
        return leaveRequestMapper.toDTO(saved);
    }
}

