package com.betopia.hrm.services.attendance.shiftRotationPattern.impl;

import com.betopia.hrm.domain.attendance.entity.Shift;
import com.betopia.hrm.domain.attendance.entity.ShiftRotationPatternDetail;
import com.betopia.hrm.domain.attendance.entity.ShiftRotationPatterns;
import com.betopia.hrm.domain.attendance.exception.ShiftRotationPatternNotFoundException;
import com.betopia.hrm.domain.attendance.repository.ShiftRotationPatternRepository;
import com.betopia.hrm.domain.attendance.request.ShiftRotationPatternsRequest;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.repository.BusinessUnitRepository;
import com.betopia.hrm.domain.company.repository.DepartmentRepository;
import com.betopia.hrm.domain.company.repository.TeamRepository;
import com.betopia.hrm.domain.company.repository.WorkplaceGroupRepository;
import com.betopia.hrm.domain.dto.attendance.ShiftDTO;
import com.betopia.hrm.domain.dto.attendance.ShiftRotationPatternDTO;
import com.betopia.hrm.domain.dto.attendance.mapper.ShiftRotationPatternMapper;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.repository.CompanyRepository;
import com.betopia.hrm.domain.users.repository.WorkplaceRepository;
import com.betopia.hrm.services.attendance.shiftRotationPattern.ShiftRotationPatternService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ShiftRotationPatternServiceImpl implements ShiftRotationPatternService {

    private final ShiftRotationPatternRepository shiftRotationPatternRepository;

    private final CompanyRepository companyRepository;

    private final BusinessUnitRepository businessUnitRepository;

    private final WorkplaceGroupRepository workplaceGroupRepository;

    private final WorkplaceRepository workplaceRepository;

    private final DepartmentRepository departmentRepository;

    private final TeamRepository teamRepository;

    private final ShiftRotationPatternMapper shiftRotationPatternMapper;

    public ShiftRotationPatternServiceImpl(ShiftRotationPatternRepository shiftRotationPatternRepository, CompanyRepository companyRepository, BusinessUnitRepository businessUnitRepository, WorkplaceGroupRepository workplaceGroupRepository, WorkplaceRepository workplaceRepository, DepartmentRepository departmentRepository, TeamRepository teamRepository, ShiftRotationPatternMapper shiftRotationPatternMapper) {
        this.shiftRotationPatternRepository = shiftRotationPatternRepository;
        this.companyRepository = companyRepository;
        this.businessUnitRepository = businessUnitRepository;
        this.workplaceGroupRepository = workplaceGroupRepository;
        this.workplaceRepository = workplaceRepository;
        this.departmentRepository = departmentRepository;
        this.teamRepository = teamRepository;
        this.shiftRotationPatternMapper = shiftRotationPatternMapper;
    }

    @Override
    public PaginationResponse<ShiftRotationPatternDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<ShiftRotationPatterns> shiftRotationPatternsPage = shiftRotationPatternRepository.findAll(pageable);

        // Get content from page
        List<ShiftRotationPatterns> shiftRotationPatterns = shiftRotationPatternsPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<ShiftRotationPatternDTO> shiftRotationPatternDTOS = shiftRotationPatternMapper.toDTOList(shiftRotationPatterns);

        // Create pagination response
        PaginationResponse<ShiftRotationPatternDTO> response = new PaginationResponse<>();
        response.setData(shiftRotationPatternDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All shift Rotation Pattern fetched successfully");

        // Set links
        Links links = Links.fromPage(shiftRotationPatternsPage, "/shift-rotation-pattern");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(shiftRotationPatternsPage, "/shift-rotation-pattern");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<ShiftRotationPatternDTO> getAllShiftRotationPattern() {
        List<ShiftRotationPatterns> shiftRotationPatterns = shiftRotationPatternRepository.findAll();

        return shiftRotationPatternMapper.toDTOList(shiftRotationPatterns);
    }

    @Override
    public ShiftRotationPatternDTO store(ShiftRotationPatternsRequest request) {

        ShiftRotationPatterns shiftRotationPatterns = new ShiftRotationPatterns();

        shiftRotationPatterns.setPatternName(request.patternName());

       shiftRotationPatterns.setCompany(companyRepository.findById(request.companyId())
               .orElseThrow(() -> new CompanyNotFound("Company not found")));

        shiftRotationPatterns.setBusinessUnit(businessUnitRepository.findById(request.businessUnitId())
                .orElseThrow(() -> new RuntimeException("Business unit not found")));

        shiftRotationPatterns.setWorkplaceGroup(workplaceGroupRepository.findById(request.workplaceGroupId())
                .orElseThrow(() -> new RuntimeException("Workplace group not found")));

        shiftRotationPatterns.setWorkPlace(workplaceRepository.findById(request.workPlaceId())
                .orElseThrow(() -> new RuntimeException("Workplace not found")));

        shiftRotationPatterns.setDepartment(departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new RuntimeException("Department not found")));

        shiftRotationPatterns.setTeam(teamRepository.findById(request.teamId())
                .orElseThrow(() -> new RuntimeException("Team not found")));

        shiftRotationPatterns.setDescription(request.description());
        shiftRotationPatterns.setRotationDays(request.rotationDays());
        shiftRotationPatterns.setStatus(request.status());

        // Handle pattern details
        if (request.shiftRotationPatternDetails() != null && !request.shiftRotationPatternDetails().isEmpty()) {
            List<ShiftRotationPatternDetail> details = request.shiftRotationPatternDetails().stream()
                    .map(detailRequest -> {
                        ShiftRotationPatternDetail detail = new ShiftRotationPatternDetail();

                        detail.setDayNumber(detailRequest.dayNumber());
                        detail.setShiftId(detailRequest.shiftId());
                        detail.setOffDay(detailRequest.isOffDay());
                        detail.setPattern(shiftRotationPatterns);

                        return detail;
                    })
                    .toList();

            shiftRotationPatterns.setPatternDetails(details);
        }

        // Save parent (cascade will save children too)
        ShiftRotationPatterns saved = shiftRotationPatternRepository.save(shiftRotationPatterns);

        // Map to DTO for response
        return shiftRotationPatternMapper.toDTO(saved);
    }

    @Override
    public ShiftRotationPatternDTO show(Long shiftRotationPatternId) {
        ShiftRotationPatterns shiftRotationPatterns = shiftRotationPatternRepository.findById(shiftRotationPatternId)
                .orElseThrow(() -> new ShiftRotationPatternNotFoundException("Shift Roration not found " + shiftRotationPatternId));

        return shiftRotationPatternMapper.toDTO(shiftRotationPatterns);
    }

    @Override
    public ShiftRotationPatternDTO update(Long shiftRotationPatternId, ShiftRotationPatternsRequest request) {
        // 1. Find existing parent
        ShiftRotationPatterns shiftRotationPatterns = shiftRotationPatternRepository.findById(shiftRotationPatternId)
                .orElseThrow(() -> new ShiftRotationPatternNotFoundException("Shift Rotation not found: " + shiftRotationPatternId));

        // 2. Update parent fields
        shiftRotationPatterns.setPatternName(request.patternName() != null ? request.patternName() : shiftRotationPatterns.getPatternName());

        shiftRotationPatterns.setCompany(companyRepository.findById(request.companyId())
                .orElseThrow(() -> new CompanyNotFound("Company not found")));

        shiftRotationPatterns.setBusinessUnit(businessUnitRepository.findById(request.businessUnitId())
                .orElseThrow(() -> new RuntimeException("Business unit not found")));

        shiftRotationPatterns.setWorkplaceGroup(workplaceGroupRepository.findById(request.workplaceGroupId())
                .orElseThrow(() -> new RuntimeException("Workplace group not found")));

        shiftRotationPatterns.setWorkPlace(workplaceRepository.findById(request.workPlaceId())
                .orElseThrow(() -> new RuntimeException("Workplace not found")));

        shiftRotationPatterns.setDepartment(departmentRepository.findById(request.departmentId())
                .orElseThrow(() -> new RuntimeException("Department not found")));

        shiftRotationPatterns.setTeam(teamRepository.findById(request.teamId())
                .orElseThrow(() -> new RuntimeException("Team not found")));

        shiftRotationPatterns.setDescription(
                request.description() != null ? request.description() : shiftRotationPatterns.getDescription()
        );
        shiftRotationPatterns.setRotationDays(
                request.rotationDays() != null ? request.rotationDays() : shiftRotationPatterns.getRotationDays()
        );
        shiftRotationPatterns.setStatus(
                request.status() != null ? request.status() : shiftRotationPatterns.getStatus()
        );

        // Handle pattern details
        if (request.shiftRotationPatternDetails() != null) {

            List<ShiftRotationPatternDetail> updatedDetails = new ArrayList<>();

            for (ShiftRotationPatternsRequest.ShiftRotationPatternDetail detailRequest : request.shiftRotationPatternDetails()) {
                ShiftRotationPatternDetail detail;

                if (detailRequest.id() != null) {
                    // 🟢 Existing detail — update it
                    detail = shiftRotationPatterns.getPatternDetails().stream()
                            .filter(d -> d.getId().equals(detailRequest.id()))
                            .findFirst()
                            .orElseThrow(() ->
                                    new RuntimeException("Pattern detail with ID " + detailRequest.id() + " not found"));

                    detail.setDayNumber(detailRequest.dayNumber());
                    detail.setShiftId(detailRequest.shiftId());
                    detail.setOffDay(detailRequest.isOffDay());
                } else {
                    // 🆕 New detail — add it
                    detail = new ShiftRotationPatternDetail();
                    detail.setDayNumber(detailRequest.dayNumber());
                    detail.setShiftId(detailRequest.shiftId());
                    detail.setOffDay(detailRequest.isOffDay());
                    detail.setPattern(shiftRotationPatterns);
                }

                updatedDetails.add(detail);
            }

            // 🧹 Replace old list (orphanRemoval = true handles deletions)
            shiftRotationPatterns.getPatternDetails().clear();
            shiftRotationPatterns.getPatternDetails().addAll(updatedDetails);
        }

        // Save parent (cascade saves children)
        ShiftRotationPatterns saved = shiftRotationPatternRepository.save(shiftRotationPatterns);

        // Return DTO
        return shiftRotationPatternMapper.toDTO(saved);
    }

    @Override
    public void destroy(Long shiftRotationPatternId) {
        ShiftRotationPatterns shiftRotationPatterns = shiftRotationPatternRepository.findById(shiftRotationPatternId)
                .orElseThrow(() -> new ShiftRotationPatternNotFoundException("Shift Rotation not found: " + shiftRotationPatternId));

        try {
            shiftRotationPatternRepository.delete(shiftRotationPatterns);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to delete Shift Rotation Pattern with ID: " + shiftRotationPatternId, ex);
        }

    }
}
