package com.betopia.hrm.domain.leave.mapper;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.leave.entity.LeaveGroupAssign;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.leave.repository.LeaveGroupAssignRepository;
import com.betopia.hrm.domain.leave.repository.LeaveTypeRepository;
import com.betopia.hrm.domain.leave.request.LeaveRequestCreateRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ManualLeaveRequestMapper {

    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveGroupAssignRepository leaveGroupAssignRepository;

    public ManualLeaveRequestMapper(EmployeeRepository employeeRepository,
                                    LeaveTypeRepository leaveTypeRepository,
                                    LeaveGroupAssignRepository leaveGroupAssignRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveGroupAssignRepository = leaveGroupAssignRepository;
    }

    /**
     * Map CreateRequest DTO -> Entity
     */
    public LeaveRequest toEntity(LeaveRequestCreateRequest request) {
        LeaveRequest leaveRequest = new LeaveRequest();
        updateEntity(leaveRequest, request);
        return leaveRequest;
    }

    /**
     * Update Entity fields from CreateRequest
     */
    public void updateEntity(LeaveRequest entity, LeaveRequestCreateRequest request) {
        Employee employee = employeeRepository.findById(request.employeeId())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with id: " + request.employeeId()));

        LeaveType leaveType = leaveTypeRepository.findById(request.leaveTypeId())
                .orElseThrow(() -> new EntityNotFoundException("LeaveType not found with id: " + request.leaveTypeId()));

        /*LeaveGroupAssign leaveGroupAssign = leaveGroupAssignRepository.findById(request.leaveGroupAssignId())
                .orElseThrow(() -> new EntityNotFoundException("LeaveGroupAssign not found with id: " + request.leaveGroupAssignId()));*/

        entity.setId(request.id());
        entity.setEmployeeId(employee.getId());
        entity.setLeaveType(leaveType);
        entity.setLeaveGroupAssign(null);
        entity.setStartDate(request.startDate());
        entity.setEndDate(request.endDate());
        entity.setDaysRequested(request.daysRequested());
        entity.setReason(request.reason());
        entity.setProofDocumentPath(request.proofDocumentPath());
        entity.setJustification(request.justification());
        entity.setRequestedAt(LocalDateTime.now());
        entity.setStatus(request.status());
    }
}
