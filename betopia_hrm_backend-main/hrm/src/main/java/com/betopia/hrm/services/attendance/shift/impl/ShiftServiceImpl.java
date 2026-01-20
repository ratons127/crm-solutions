package com.betopia.hrm.services.attendance.shift.impl;

import com.betopia.hrm.domain.attendance.entity.Shift;
import com.betopia.hrm.domain.attendance.entity.ShiftWeeklyOff;
import com.betopia.hrm.domain.attendance.repository.ShiftCategoryRepository;
import com.betopia.hrm.domain.attendance.repository.ShiftRepository;
import com.betopia.hrm.domain.attendance.request.ShiftRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ShiftDTO;
import com.betopia.hrm.domain.dto.attendance.mapper.ShiftMapper;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.services.attendance.shift.ShiftService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepository;

    private final ShiftCategoryRepository shiftCategoryRepository;

    private final CompanyRepository companyRepository;


    private final ShiftMapper shiftMapper;

    public ShiftServiceImpl(ShiftRepository shiftRepository, ShiftCategoryRepository shiftCategoryRepository, CompanyRepository companyRepository, ShiftMapper shiftMapper) {
        this.shiftRepository = shiftRepository;
        this.shiftCategoryRepository = shiftCategoryRepository;
        this.companyRepository = companyRepository;
        this.shiftMapper = shiftMapper;
    }

    @Override
    public PaginationResponse<ShiftDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<Shift> shiftPage = shiftRepository.findAll(pageable);

        // Get content from page
        List<Shift> shifts = shiftPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<ShiftDTO> shiftDTOS = shiftMapper.toDTOList(shifts);

        // Create pagination response
        PaginationResponse<ShiftDTO> response = new PaginationResponse<>();
        response.setData(shiftDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All shift fetched successfully");

        // Set links
        Links links = Links.fromPage(shiftPage, "/shift");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(shiftPage, "/shift");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<ShiftDTO> getAllShift() {
        List<Shift> shifts = shiftRepository.findAll();
        return shiftMapper.toDTOList(shifts);
    }

    @Override
    public ShiftDTO store(ShiftRequest request) {
        Shift shift = new Shift();

        shift.setShiftName(request.shiftName());
        shift.setShiftCode(request.shiftCode());

        shift.setShiftCategory(shiftCategoryRepository.findById(request.shiftCategoryId())
                .orElseThrow(() -> new RuntimeException("Shift category not found")));

        shift.setCompany(companyRepository.findById(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company not found")));


        shift.setStartTime(request.startTime());
        shift.setEndTime(request.endTime());
        shift.setBreakMinutes(request.breakMinutes());
        shift.setNightShift(request.isNightShift());
        shift.setGraceInMinutes(request.graceInMinutes());
        shift.setGraceOutMinutes(request.graceOutMinutes());
        shift.setStatus(request.status());

        if (request.weeklyOffs() != null && !request.weeklyOffs().isEmpty()) {
            var weeklyOffList = request.weeklyOffs().stream()
                    .map(wo -> {
                        ShiftWeeklyOff off = new ShiftWeeklyOff();
                        off.setDayOfWeek(wo.dayOfWeek());
                        off.setShift(shift);
                        return off;
                    })
                    .collect(Collectors.toList());
            shift.setWeeklyOffs(weeklyOffList);
        }

        // Save entity
        Shift savedShift = shiftRepository.save(shift);

        // Convert Entity to DTO and return
        return shiftMapper.toDTO(savedShift);
    }

    @Override
    public ShiftDTO show(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found with id: " + shiftId));
        return shiftMapper.toDTO(shift);
    }

    @Override
    public ShiftDTO update(Long shiftId, ShiftRequest request) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found with id: " + shiftId));

        // 1 update basic fields
        shift.setShiftName(request.shiftName() != null ? request.shiftName() : shift.getShiftName());
        shift.setShiftCode(request.shiftCode() != null ? request.shiftCode() : shift.getShiftCode());

        shift.setShiftCategory(shiftCategoryRepository.findById(request.shiftCategoryId())
                .orElseThrow(() -> new RuntimeException("Shift category not found")));

        shift.setCompany(companyRepository.findById(request.companyId())
                .orElseThrow(() -> new RuntimeException("Company not found")));


        shift.setStartTime(request.startTime() != null ? request.startTime() : shift.getStartTime());
        shift.setEndTime(request.endTime() != null ? request.endTime() : shift.getEndTime());
        shift.setBreakMinutes(request.breakMinutes() != null ? request.breakMinutes() : shift.getBreakMinutes());
        shift.setNightShift(request.isNightShift() != null ? request.isNightShift() : shift.getNightShift());
        shift.setGraceInMinutes(request.graceInMinutes() != null ? request.graceInMinutes() : shift.getGraceInMinutes());
        shift.setGraceOutMinutes(request.graceOutMinutes() != null ? request.graceOutMinutes() : shift.getGraceOutMinutes());
        shift.setStatus(request.status());

        // 3 update weeklyOffs
        if (request.weeklyOffs() != null) {
            // Map request weeklyOffs by ID (null means new)
            List<ShiftWeeklyOff> updatedWeeklyOffs = new ArrayList<>();

            for (ShiftRequest.WeeklyOffRequest woRequest : request.weeklyOffs()) {
                ShiftWeeklyOff off;

                if (woRequest.id() != null) {
                    // Existing weeklyOff — update it
                    off = shift.getWeeklyOffs().stream()
                            .filter(w -> w.getId().equals(woRequest.id()))
                            .findFirst()
                            .orElseThrow(() -> new RuntimeException("WeeklyOff id " + woRequest.id() + " not found"));

                    off.setDayOfWeek(woRequest.dayOfWeek());
                } else {
                    // New weeklyOff — add it
                    off = new ShiftWeeklyOff();
                    off.setDayOfWeek(woRequest.dayOfWeek());
                    off.setShift(shift);
                }

                updatedWeeklyOffs.add(off);
            }

            // Replace old weeklyOffs with new list (orphanRemoval will delete missing ones)
            shift.getWeeklyOffs().clear();
            shift.getWeeklyOffs().addAll(updatedWeeklyOffs);
        }

        // 4 updated Shift
        Shift savedShift = shiftRepository.save(shift);

        // 5 return DTO
        return shiftMapper.toDTO(savedShift);
    }

    @Override
    public void destroy(Long shiftId) {
        Shift shift = shiftRepository.findById(shiftId)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        // This will automatically delete associated weekly offs because of orphanRemoval = true
        shiftRepository.delete(shift);
    }
}
