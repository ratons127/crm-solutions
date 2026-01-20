package com.betopia.hrm.services.leaves.leaveeligibilityrules.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveEligibilityRules;
import com.betopia.hrm.domain.leave.exception.leavetype.LeaveTypeNotFoundException;
import com.betopia.hrm.domain.leave.repository.LeaveEligibityRulesRepository;
import com.betopia.hrm.domain.leave.repository.LeaveGroupRepository;
import com.betopia.hrm.domain.leave.repository.LeaveTypeRepository;
import com.betopia.hrm.domain.leave.request.LeaveEligibilityRulesRequest;
import com.betopia.hrm.services.leaves.leaveeligibilityrules.LeaveEligibilityRulesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveEligibilityRulesServiceImpl implements LeaveEligibilityRulesService {

    private final LeaveEligibityRulesRepository leaveEligibityRulesRepository ;

    private final LeaveTypeRepository leaveTypeRepository ;

    private final LeaveGroupRepository leaveGroupRepository;

    public LeaveEligibilityRulesServiceImpl(LeaveEligibityRulesRepository leaveEligibityRulesRepository, LeaveTypeRepository leaveTypeRepository
    ,LeaveGroupRepository leaveGroupRepository) {
        this.leaveEligibityRulesRepository = leaveEligibityRulesRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveGroupRepository = leaveGroupRepository;
    }

    @Override
    public PaginationResponse<LeaveEligibilityRules> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<LeaveEligibilityRules> leaveEligibilityRulesPage = leaveEligibityRulesRepository.findAll(pageable);
        List<LeaveEligibilityRules> leaveEligibityRules = leaveEligibilityRulesPage.getContent();

        PaginationResponse<LeaveEligibilityRules> response = new PaginationResponse<>();
        response.setData(leaveEligibityRules);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All leave eligibility rules fetched successfully");

        Links links = Links.fromPage(leaveEligibilityRulesPage, "/leave-eligibility-rules");
        response.setLinks(links);

        Meta meta = Meta.fromPage(leaveEligibilityRulesPage, "/leave-eligibility-rules");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<LeaveEligibilityRules> getAllLeaveEligibilityRules() {
        return leaveEligibityRulesRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public LeaveEligibilityRules getLeaveEligibilityRulesById(Long id) {
        return leaveEligibityRulesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave eligibility rules not found with id: " + id));
    }

    @Override
    public LeaveEligibilityRules store(LeaveEligibilityRulesRequest request) {
        LeaveEligibilityRules leaveEligibilityRules = new LeaveEligibilityRules();
        leaveEligibilityRules.setGender(request.gender());
        leaveEligibilityRules.setMinTenureMonths(request.minTenureMonths());
        leaveEligibilityRules.setMaxTenureMonths(request.maxTenureMonths());
        leaveEligibilityRules.setEmploymentStatus(request.employmentStatus());
        leaveEligibilityRules.setStatus(request.status());

        leaveEligibilityRules.setLeaveType(leaveTypeRepository.findById(request.leaveTypeId())
                .orElseThrow(() -> new LeaveTypeNotFoundException("Leave type rules not found with id: " + request.leaveTypeId())));

        leaveEligibilityRules.setLeaveGroup(leaveGroupRepository.findById(request.leaveGroupId())
                .orElseThrow(() -> new LeaveTypeNotFoundException("Leave group not found with id: " + request.leaveGroupId())));

        return leaveEligibityRulesRepository.save(leaveEligibilityRules);
    }

    @Override
    public LeaveEligibilityRules updateLeaveEligibilityRules(Long id, LeaveEligibilityRulesRequest request) {
        LeaveEligibilityRules leaveEligibityRules = leaveEligibityRulesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave Eligibility Rules not found with id: " + id));

        leaveEligibityRules.setGender(request.gender() != null ? request.gender() : leaveEligibityRules.getGender());
        leaveEligibityRules.setMinTenureMonths(request.minTenureMonths() != null ? request.minTenureMonths() : leaveEligibityRules.getMinTenureMonths());
        leaveEligibityRules.setMaxTenureMonths(request.maxTenureMonths() != null ? request.maxTenureMonths() : leaveEligibityRules.getMaxTenureMonths());
        leaveEligibityRules.setEmploymentStatus(request.employmentStatus() != null ? request.employmentStatus() : leaveEligibityRules.getEmploymentStatus());
        leaveEligibityRules.setStatus(request.status() != null ? request.status() : leaveEligibityRules.getStatus());

        leaveEligibityRules.setLeaveType(request.leaveTypeId() != null ? leaveTypeRepository.findById(request.leaveTypeId()).orElse(null) : leaveEligibityRules.getLeaveType());

        leaveEligibityRules.setLeaveGroup(request.leaveGroupId() != null ? leaveGroupRepository.findById(request.leaveGroupId()).orElse(null) : leaveEligibityRules.getLeaveGroup());

        leaveEligibityRules.setLastModifiedDate(LocalDateTime.now());

        return leaveEligibityRulesRepository.save(leaveEligibityRules);
    }

    @Override
    public void deleteLeaveEligibilityRules(Long id) {

        LeaveEligibilityRules leaveEligibilityRules = leaveEligibityRulesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave Eligibility Rules not found with id: " + id));

        leaveEligibilityRules.setDeletedAt(LocalDateTime.now());

        leaveEligibityRulesRepository.delete(leaveEligibilityRules);

    }
}
