package com.betopia.hrm.services.leaves.leavetyperules.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveTypeRules;
import com.betopia.hrm.domain.leave.exception.leavetype.LeaveTypeNotFoundException;
import com.betopia.hrm.domain.leave.repository.LeaveTypeRepository;
import com.betopia.hrm.domain.leave.repository.LeaveTypeRulesRepository;
import com.betopia.hrm.domain.leave.request.LeaveTypeRulesRequest;
import com.betopia.hrm.services.leaves.leavetyperules.LeaveTypeRulesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveTypeRulesServiceImpl implements LeaveTypeRulesService {

    private final LeaveTypeRulesRepository leaveTypeRulesRepository ;

    private final LeaveTypeRepository leaveTypeRepository ;

    public LeaveTypeRulesServiceImpl(LeaveTypeRulesRepository leaveTypeRulesRepository, LeaveTypeRepository leaveTypeRepository) {
        this.leaveTypeRulesRepository = leaveTypeRulesRepository;
        this.leaveTypeRepository = leaveTypeRepository;
    }


    @Override
    public PaginationResponse<LeaveTypeRules> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<LeaveTypeRules> leaveTypeRulesPage = leaveTypeRulesRepository.findAll(pageable);
        List<LeaveTypeRules> leaveTypeRules = leaveTypeRulesPage.getContent();

        PaginationResponse<LeaveTypeRules> response = new PaginationResponse<>();
        response.setData(leaveTypeRules);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All leave type rules fetched successfully");

        Links links = Links.fromPage(leaveTypeRulesPage, "/leave-type-rules");
        response.setLinks(links);

        Meta meta = Meta.fromPage(leaveTypeRulesPage, "/leave-type-rules");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<LeaveTypeRules> getAllLeaveTypeRules() {
        return leaveTypeRulesRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public LeaveTypeRules getLeaveTypeRulesById(Long id) {
        return leaveTypeRulesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave Type rules not found with id: " + id));
    }

    @Override
    public LeaveTypeRules store(LeaveTypeRulesRequest request) {

        LeaveTypeRules leaveTypeRules = new LeaveTypeRules();
        leaveTypeRules.setRuleKey(request.ruleKey());
        leaveTypeRules.setRuleValue(request.ruleValue());
        leaveTypeRules.setDescription(request.description());
        leaveTypeRules.setStatus(request.status());

        leaveTypeRules.setLeaveType(leaveTypeRepository.findById(request.leaveTypeId())
                .orElseThrow(() -> new LeaveTypeNotFoundException("Leave type rules not found with id: " + request.leaveTypeId())));
        return leaveTypeRulesRepository.save(leaveTypeRules);
    }

    @Override
    public LeaveTypeRules updateLeaveTypeRules(Long id, LeaveTypeRulesRequest request) {
        LeaveTypeRules leaveTypeRules = leaveTypeRulesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave Type Rules not found with id: " + id));

        leaveTypeRules.setRuleKey(request.ruleKey() != null ? request.ruleKey() : leaveTypeRules.getRuleKey());
        leaveTypeRules.setRuleValue(request.ruleValue() != null ? request.ruleValue() : leaveTypeRules.getRuleValue());
        leaveTypeRules.setDescription(request.description() != null ? request.description() : leaveTypeRules.getDescription());
        leaveTypeRules.setStatus(request.status() != null ? request.status() : leaveTypeRules.getStatus());

        leaveTypeRules.setLeaveType(request.leaveTypeId() != null ? leaveTypeRepository.findById(request.leaveTypeId()).orElse(null) : leaveTypeRules.getLeaveType());

        leaveTypeRules.setLastModifiedDate(LocalDateTime.now());

        return leaveTypeRulesRepository.save(leaveTypeRules);
    }

    @Override
    public void deleteLeaveTypeRules(Long id) {

        LeaveTypeRules leaveTypeRules = leaveTypeRulesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveTypeRules not found with id: " + id));

        leaveTypeRules.setDeletedAt(LocalDateTime.now());

        leaveTypeRulesRepository.delete(leaveTypeRules);

    }
}
