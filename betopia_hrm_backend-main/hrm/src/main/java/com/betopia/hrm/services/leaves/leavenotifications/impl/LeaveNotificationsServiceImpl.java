package com.betopia.hrm.services.leaves.leavenotifications.impl;

import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeaveNotificationsDTO;
import com.betopia.hrm.domain.dto.leave.mapper.LeaveNotificationsMapper;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.leave.entity.LeaveApprovals;
import com.betopia.hrm.domain.leave.entity.LeaveNotifications;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.repository.LeaveApprovalsRepository;
import com.betopia.hrm.domain.leave.repository.LeaveNotificationsRepository;
import com.betopia.hrm.domain.leave.request.LeaveApprovalsRequest;
import com.betopia.hrm.domain.leave.request.LeaveNotificationsRequest;
import com.betopia.hrm.services.leaves.leavenotifications.LeaveNotificationsService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveNotificationsServiceImpl implements LeaveNotificationsService {

    public final LeaveNotificationsRepository leaveNotificationsRepository;
    public final LeaveApprovalsRepository leaveApprovalsRepository;
    public final LeaveNotificationsMapper leaveNotificationsMapper;
    private final EmployeeRepository employeeRepository;

    public LeaveNotificationsServiceImpl(LeaveNotificationsRepository leaveNotificationsRepository,
                                         LeaveApprovalsRepository leaveApprovalsRepository,
                                         LeaveNotificationsMapper leaveNotificationsMapper,
                                         EmployeeRepository employeeRepository) {
        this.leaveNotificationsMapper = leaveNotificationsMapper;
        this.leaveNotificationsRepository = leaveNotificationsRepository;
        this.leaveApprovalsRepository = leaveApprovalsRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public PaginationResponse<LeaveNotificationsDTO> index(Sort.Direction direction, int page, int perPage) {
        return null;
    }

    @Override
    public List<LeaveNotificationsDTO> getAll() {
        return List.of();
    }

    @Override
    public LeaveNotificationsDTO store(LeaveNotificationsRequest request) {
        return null;
    }

    @Override
    public LeaveNotificationsDTO show(Long id) {
        return null;
    }

    @Override
    public LeaveNotificationsDTO update(Long id, LeaveNotificationsRequest request) {
        return null;
    }

    @Override
    public void destroy(Long id) {

    }

    @Override
    public LeaveNotificationsDTO createInitialNotification(LeaveApprovals request) {

        LeaveApprovals approvals = leaveApprovalsRepository.findById(request.getId())
                .orElseThrow(() -> new RuntimeException("Leave approval not found with id: " + request.getId()));

//
//        Employee sender = employeeRepository.findById(request.getLeaveRequest().getEmployeeId())
//                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + request.getLeaveRequest().getEmployeeId()));
//
//
//        Employee receipient = employeeRepository.findById(request.getApproverId())
//                .orElseThrow(() -> new IllegalStateException("Supervisor not found with id: " + request.getApproverId()));

        LeaveNotifications notification = new LeaveNotifications();

        notification.setLeaveApproval(approvals);
        notification.setSender(approvals.getEmployee().getId());
        notification.setRecipient(approvals.getApproverId());
        notification.setType("LEAVE_REQUEST_SUBMITTED");
        notification.setMessage("New leave request pending your approval.Need your approval");
        notification.setNotificationStatus("UNREAD");
        LeaveNotifications saved = leaveNotificationsRepository.save(notification);
        return leaveNotificationsMapper.toDTO(saved);
    }

    @Override
    public String sendNotificationMessageByLeaveRequest(Long id) {
        // Find the notification linked to this leave request
        LeaveNotifications notification = leaveNotificationsRepository
                .findByLeaveApproval_LeaveRequest_Id(id)
                .orElseThrow(() -> new RuntimeException("Notification not found for Leave Request ID: " + id));

        String msg = String.format("📩 Notification for leave approval: %s", notification.getMessage());

        System.out.println("=== Sending Notification ===");
        System.out.println(msg);
        System.out.println("============================");

        // Mark as sent
        notification.setNotificationStatus("SENT");
        leaveNotificationsRepository.save(notification);


        return "Notification sent successfully for ID: " + id;
    }

    @Override
    public String sendNotificationAfterApproval(Long leaveRequestId) {
        LeaveNotifications previousNotification = leaveNotificationsRepository
                .findByLeaveApproval_LeaveRequest_Id(leaveRequestId)
                .orElseThrow(() -> new RuntimeException("No existing notification found for leave request ID: " + leaveRequestId));

        // Step 2: Swap sender and recipient
        Long oldSender = previousNotification.getSender();
        Long oldRecipient = previousNotification.getRecipient();

        // Step 3: Get leave details
        LeaveApprovals approval = previousNotification.getLeaveApproval();
        LeaveRequest request = approval.getLeaveRequest();
        String leaveTypeName = request.getLeaveType().getName();
        String status = request.getStatus().name(); // APPROVED / REJECTED / etc.

        // Step 4: Create new notification
        previousNotification.setLeaveApproval(approval);
        previousNotification.setSender(oldRecipient);   // supervisor now becomes sender
        previousNotification.setRecipient(oldSender);   // employee becomes recipient
        previousNotification.setType("LEAVE_STATUS_UPDATE");
        previousNotification.setMessage(String.format("Your leave for %s is %s.", leaveTypeName, status));
        previousNotification.setNotificationStatus("SENT TO EMPLOYEE");

       leaveNotificationsRepository.save(previousNotification);

        // ✅ 7. Log / return confirmation message
        String msg = String.format("📢 %s", previousNotification.getMessage());
        System.out.println("=== Notification Updated ===");
        System.out.println(msg);
        System.out.println("============================");

        return msg;
    }


}
