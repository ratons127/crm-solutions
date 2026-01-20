package com.betopia.hrm.services.leaves.leavegroup.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveGroup;
import com.betopia.hrm.domain.leave.repository.LeaveGroupRepository;
import com.betopia.hrm.domain.leave.request.LeaveGroupRequest;
import com.betopia.hrm.services.leaves.leavegroup.LeaveGroupService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveGroupServiceImpl implements LeaveGroupService {

    private final LeaveGroupRepository leaveGroupRepository;

    public LeaveGroupServiceImpl(LeaveGroupRepository leaveGroupRepository) {
        this.leaveGroupRepository = leaveGroupRepository;
    }

    @Override
    public PaginationResponse<LeaveGroup> index(Sort.Direction direction, int page, int perPage) {

        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<LeaveGroup> leaveTypePage = leaveGroupRepository.findAll(pageable);
        List<LeaveGroup> leaveGroups = leaveTypePage.getContent();

        PaginationResponse<LeaveGroup> response = new PaginationResponse<>();
        response.setData(leaveGroups);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All leave groups fetched successfully");

        Links links = Links.fromPage(leaveTypePage, "/leave-groups");
        response.setLinks(links);

        Meta meta = Meta.fromPage(leaveTypePage, "/leave-groups");
        response.setMeta(meta);

        return response;
    }


    @Override
    public List<LeaveGroup> getAllLeaveGroups() {
        return leaveGroupRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public LeaveGroup getLeaveGroupById(Long id) {
        return leaveGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveGroup not found with id: " + id));
    }

    @Override
    public LeaveGroup store(LeaveGroupRequest request) {

        LeaveGroup leaveGroup = new LeaveGroup();
        leaveGroup.setName(request.name());
        leaveGroup.setDescription(request.description());
        leaveGroup.setStatus(request.status());
        return leaveGroupRepository.save(leaveGroup);
    }

    @Override
    public LeaveGroup updateLeaveGroup(Long id, LeaveGroupRequest request) {

        LeaveGroup leaveGroup = leaveGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveGroup not found with id: " + id));

        leaveGroup.setName(request.name() != null ? request.name() : leaveGroup.getName());
        leaveGroup.setDescription(request.description() != null ? request.description() : leaveGroup.getDescription());
        leaveGroup.setStatus(request.status() != null ? request.status() : leaveGroup.getStatus());
        leaveGroup.setLastModifiedDate(LocalDateTime.now());

        return leaveGroupRepository.save(leaveGroup);
    }

    @Override
    public void deleteLeaveGroup(Long id) {

        LeaveGroup leaveGroup = leaveGroupRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveGroup not found with id: " + id));

        leaveGroup.setDeletedAt(LocalDateTime.now());

        leaveGroupRepository.delete(leaveGroup);

    }
}
