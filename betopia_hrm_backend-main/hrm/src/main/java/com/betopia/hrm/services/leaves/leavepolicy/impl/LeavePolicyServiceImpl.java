package com.betopia.hrm.services.leaves.leavepolicy.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.leave.LeavePolicyDTO;
import com.betopia.hrm.domain.dto.leave.mapper.LeavePolicyMapper;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.mapper.ManualLeavePolicyMapper;
import com.betopia.hrm.domain.leave.repository.LeavePolicyRepository;
import com.betopia.hrm.domain.leave.request.LeavePolicyRequest;
import com.betopia.hrm.services.leaves.leavepolicy.LeavePolicyService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeavePolicyServiceImpl implements LeavePolicyService {

    private final LeavePolicyRepository leavePolicyRepository;
    private final ManualLeavePolicyMapper manualLeavePolicyMapper;
    private final LeavePolicyMapper leavePolicyMapper;

    public LeavePolicyServiceImpl(
            LeavePolicyRepository leavePolicyRepository,
            ManualLeavePolicyMapper manualLeavePolicyMapper,
            LeavePolicyMapper leavePolicyMapper
    ) {
        this.leavePolicyRepository = leavePolicyRepository;
        this.manualLeavePolicyMapper = manualLeavePolicyMapper;
        this.leavePolicyMapper = leavePolicyMapper;
    }

    @Override
    public PaginationResponse<LeavePolicyDTO> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<LeavePolicy> leavePolicyPage = leavePolicyRepository.findAll(pageable);
        List<LeavePolicyDTO> leavePolicies = leavePolicyPage.getContent()
                .stream()
                .map(leavePolicyMapper::toDTO)
                .toList();

        PaginationResponse<LeavePolicyDTO> response = new PaginationResponse<>();
        response.setData(leavePolicies);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All leave policies fetched successfully");

        Links links = Links.fromPage(leavePolicyPage, "/leave-policies");
        response.setLinks(links);

        Meta meta = Meta.fromPage(leavePolicyPage, "/leave-policies");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<LeavePolicyDTO> getAllLeavePolicies() {
        return leavePolicyRepository.findAll(Sort.by(Sort.Direction.DESC, "id"))
                .stream()
                .map(leavePolicyMapper::toDTO)
                .toList();
    }

    @Override
    public LeavePolicyDTO getLeavePolicyById(Long id) {
        return leavePolicyMapper.toDTO(leavePolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave policy not found with id: " + id)));
    }

    @Override
    public LeavePolicyDTO store(LeavePolicyRequest request) {
        LeavePolicy leavePolicy = manualLeavePolicyMapper.toEntity(request);

        validateBeforeSave(leavePolicy, null);

        return leavePolicyMapper.toDTO(leavePolicyRepository.save(leavePolicy));
    }

    @Override
    public LeavePolicyDTO updateLeavePolicy(Long id, LeavePolicyRequest request) {
        LeavePolicy leavePolicy = leavePolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave policy not found with id: " + id));

        validateBeforeSave(leavePolicy, id);

        manualLeavePolicyMapper.updateEntity(leavePolicy, request);
        leavePolicy.setLastModifiedDate(LocalDateTime.now());

        return leavePolicyMapper.toDTO(leavePolicyRepository.save(leavePolicy));
    }

    @Override
    public void deleteLeavePolicy(Long id) {
        LeavePolicy leavePolicy = leavePolicyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave policy not found with id: " + id));

        leavePolicyRepository.delete(leavePolicy);
    }

    private void validateBeforeSave(LeavePolicy policy, Long id) {

        validateLeavePolicyRules(policy);

        boolean duplicateExists = leavePolicyRepository.existsDuplicate(
                policy.getLeaveType().getId(),
                policy.getLeaveGroupAssign().getId(),
                policy.getEmployeeTypeId(),
                policy.getTenureRequiredDays(),
                id
        );

        if (duplicateExists) {
            throw new IllegalArgumentException("Duplicate leave policy exists for same configuration.");
        }
    }

    private void validateLeavePolicyRules(LeavePolicy policy) {
        Long employeeTypeId = policy.getEmployeeTypeId();
        Integer tenureDays = policy.getTenureRequiredDays();

        boolean valid =
                (employeeTypeId != null && (tenureDays == null || tenureDays == 0)) ||
                        (employeeTypeId == null && tenureDays != null && tenureDays > 0);

        if (!valid) {
            throw new IllegalArgumentException(
                    "Invalid configuration: either Employee Type must be set (and Tenure Required Days=0), " +
                            "or Tenure Required Days>0 (and Employee Type must be null)."
            );
        }
    }
}
