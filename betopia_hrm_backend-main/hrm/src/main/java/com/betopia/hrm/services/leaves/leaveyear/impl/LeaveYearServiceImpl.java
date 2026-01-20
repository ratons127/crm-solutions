package com.betopia.hrm.services.leaves.leaveyear.impl;

import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.leave.entity.LeaveYear;
import com.betopia.hrm.domain.leave.repository.LeaveYearRepository;
import com.betopia.hrm.domain.leave.request.LeaveYearRequest;
import com.betopia.hrm.services.leaves.leaveyear.LeaveYearService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LeaveYearServiceImpl implements LeaveYearService {

    private final LeaveYearRepository leaveYearRepository ;

    public LeaveYearServiceImpl(LeaveYearRepository leaveYearRepository) {
        this.leaveYearRepository = leaveYearRepository;
    }

    @Override
    public PaginationResponse<LeaveYear> index(Sort.Direction direction, int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));
        Page<LeaveYear> leaveYearPage = leaveYearRepository.findAll(pageable);
        List<LeaveYear> leaveYears = leaveYearPage.getContent();

        PaginationResponse<LeaveYear> response = new PaginationResponse<>();
        response.setData(leaveYears);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All leave years fetched successfully");

        Links links = Links.fromPage(leaveYearPage, "/leave-years");
        response.setLinks(links);

        Meta meta = Meta.fromPage(leaveYearPage, "/leave-years");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<LeaveYear> getAllLeaveYears() {
        return leaveYearRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    public LeaveYear getLeaveYearById(Long id) {
        return leaveYearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveYear not found with id: " + id));
    }

    @Override
    public LeaveYear store(LeaveYearRequest request) {
        LeaveYear leaveYear = new LeaveYear();
        leaveYear.setStartDate(request.startDate());
        leaveYear.setEndDate(request.endDate());
        leaveYear.setStatus(request.status());

        return leaveYearRepository.save(leaveYear);
    }

    @Override
    public LeaveYear updateLeaveYear(Long id, LeaveYearRequest request) {
        LeaveYear leaveYear = leaveYearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveYear not found with id: " + id));

        leaveYear.setStartDate(request.startDate() != null ? request.startDate() : leaveYear.getStartDate());
        leaveYear.setEndDate(request.endDate() != null ? request.endDate() : leaveYear.getEndDate());
        leaveYear.setStatus(request.status() != null ? request.status() : leaveYear.getStatus());
        leaveYear.setLastModifiedDate(LocalDateTime.now());

        return leaveYearRepository.save(leaveYear);
    }

    @Override
    public void deleteLeaveYear(Long id) {

        LeaveYear leaveYear = leaveYearRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("LeaveYear not found with id: " + id));

        leaveYear.setDeletedAt(LocalDateTime.now());

        leaveYearRepository.delete(leaveYear);

    }
}
