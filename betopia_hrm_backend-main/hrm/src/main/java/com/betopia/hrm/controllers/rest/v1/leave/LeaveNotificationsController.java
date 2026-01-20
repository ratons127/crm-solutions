package com.betopia.hrm.controllers.rest.v1.leave;

import com.betopia.hrm.services.leaves.leavenotifications.LeaveNotificationsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/leave-notification")
@Tag(
        name = "Leave Management -> Leave Notification",
        description = "Operations related to leave notification"
)
public class LeaveNotificationsController {

    private final LeaveNotificationsService leaveNotificationsService;

    public LeaveNotificationsController(LeaveNotificationsService leaveNotificationsService) {
        this.leaveNotificationsService = leaveNotificationsService;
    }

    /**
     * API to send a notification for a leave notification record.
     * Triggered right after the notification is created.
     */
    @PostMapping("/send/{id}")
    public ResponseEntity<String> sendNotificationMessage(@PathVariable Long id) {
        String response = leaveNotificationsService.sendNotificationMessageByLeaveRequest(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send/approval/{id}")
    public ResponseEntity<String> sendNotificationAfterApproval(@PathVariable Long id) {
        String message = leaveNotificationsService.sendNotificationAfterApproval(id);
        return ResponseEntity.ok(message);
    }

}
