package com.betopia.hrm.services.attendance.shiftassignment.impl;


import com.betopia.hrm.domain.attendance.entity.AttendanceApproval;
import com.betopia.hrm.domain.attendance.entity.Shift;
import com.betopia.hrm.domain.attendance.entity.ShiftAssignments;
import com.betopia.hrm.domain.attendance.exception.ShiftAssignmentsNotFound;
import com.betopia.hrm.domain.attendance.exception.ShiftNotFoundException;
import com.betopia.hrm.domain.attendance.repository.ShiftAssignmentsRepository;
import com.betopia.hrm.domain.attendance.repository.ShiftRepository;
import com.betopia.hrm.domain.attendance.request.ShiftAssignmentsRequest;
import com.betopia.hrm.domain.attendance.request.ShowShiftAssignRequest;
import com.betopia.hrm.domain.attendance.specification.ShowShiftSpecification;
import com.betopia.hrm.domain.base.response.pagination.Links;
import com.betopia.hrm.domain.base.response.pagination.Meta;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.attendance.ShiftAssignmentsDTO;
import com.betopia.hrm.domain.dto.attendance.ShowShiftAssignDTO;
import com.betopia.hrm.domain.dto.attendance.mapper.ShiftAssignmentsMapper;
import com.betopia.hrm.domain.dto.attendance.mapper.ShowShiftAssignDTOMapper;
import com.betopia.hrm.domain.dto.employee.EmployeeShiftAssignDTO;
import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeAreadyExist;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.employee.request.EmployeeShiftAssignRequest;
import com.betopia.hrm.domain.employee.specification.EmployeeSpecification;
import com.betopia.hrm.domain.users.entity.User;
import com.betopia.hrm.domain.users.repository.UserRepository;
import com.betopia.hrm.services.attendance.shiftassignment.ShiftAssignmentsService;
import com.betopia.hrm.webapp.util.AuthUtils;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ShiftAssignmentsServiceImpl implements ShiftAssignmentsService {

    private final ShiftAssignmentsRepository shiftAssignmentsRepository;
    private final ShiftRepository shiftRepository;
    private final EmployeeRepository employeeRepository;
    private final ShiftAssignmentsMapper shiftAssignmentsMapper;
    private final ShowShiftAssignDTOMapper showShiftAssignDTOMapper;
    private final UserRepository userRepository;

    public ShiftAssignmentsServiceImpl(ShiftAssignmentsRepository shiftAssignmentsRepository, ShiftRepository shiftRepository,
                                       EmployeeRepository employeeRepository, ShiftAssignmentsMapper shiftAssignmentsMapper,
                                       ShowShiftAssignDTOMapper showShiftAssignDTOMapper, UserRepository userRepository) {
            this.shiftAssignmentsRepository = shiftAssignmentsRepository;
            this.shiftAssignmentsMapper = shiftAssignmentsMapper;
            this.shiftRepository = shiftRepository;
            this.employeeRepository = employeeRepository;
            this.showShiftAssignDTOMapper = showShiftAssignDTOMapper;
            this.userRepository = userRepository;
    }

    @Override
    public PaginationResponse<ShiftAssignmentsDTO> index(Sort.Direction direction, int page, int perPage) {
        // Create pageable
        Pageable pageable = PageRequest.of(page - 1, perPage, Sort.by(direction, "id"));

        // Fetch paginated data
        Page<ShiftAssignments> shiftAssignmentsPage = shiftAssignmentsRepository.findAll(pageable);

        // Get content from page
        List<ShiftAssignments> shiftAssignments  = shiftAssignmentsPage.getContent();

        // Convert entities to DTOs using MapStruct
        List<ShiftAssignmentsDTO> shiftAssignmentsDTOS = shiftAssignmentsMapper.toDTOList(shiftAssignments);

        // Create pagination response
        PaginationResponse<ShiftAssignmentsDTO> response = new PaginationResponse<>();
        response.setData(shiftAssignmentsDTOS);
        response.setSuccess(true);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("All shift assignment fetched successfully");

        // Set links
        Links links = Links.fromPage(shiftAssignmentsPage, "/shift-assignment");
        response.setLinks(links);

        // Set meta
        Meta meta = Meta.fromPage(shiftAssignmentsPage, "/shift-assignment");
        response.setMeta(meta);

        return response;
    }

    @Override
    public List<ShiftAssignmentsDTO> getAll() {
        List<ShiftAssignments> shiftAssignments = shiftAssignmentsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return shiftAssignmentsMapper.toDTOList(shiftAssignments);
    }

    @Override
    public ShiftAssignmentsDTO store(ShiftAssignmentsRequest request) {
        return null;
    }

    @Override
    public ShiftAssignmentsDTO show(Long Id) {
        ShiftAssignments shiftAssignments = shiftAssignmentsRepository.findById(Id)
                .orElseThrow(() -> new ShiftAssignmentsNotFound("Shift Assignment not found " + Id));

        return shiftAssignmentsMapper.toDTO(shiftAssignments);
    }

    @Override
    public ShiftAssignmentsDTO update(Long Id, ShiftAssignmentsRequest request) {
        return null;
    }

    @Override
    public void destroy(Long Id) {
        ShiftAssignments shiftCategory = shiftAssignmentsRepository.findById(Id).orElseThrow(() ->
                new ShiftAssignmentsNotFound("Shift Assignment not found with id: " + Id));
        shiftAssignmentsRepository.delete(shiftCategory);

    }

    @Override
    @Transactional
    public List<ShiftAssignmentsDTO> assignShiftToEmployees(ShiftAssignmentsRequest request) {
        User users = null;

        Shift shift = shiftRepository.findById(request.shiftId())
                .orElseThrow(() -> new ShiftNotFoundException("Shift not found with id: " + request.shiftId()));

        if(!AuthUtils.getCurrentUsername().isEmpty()){
            String identifier = AuthUtils.getCurrentUsername();
            users = userRepository.findByEmailOrPhone(identifier,identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User not found "));
        }

        System.err.println("users "+users.getId());

//        User assignedBy = userRepository.findById(request.assignedBy())
//                .orElseThrow(() -> new EntityNotFoundException("users not found with id: " + request.assignedBy()));

        List<Employee> employees = employeeRepository.findAllById(request.employeeIds());

        if (employees.isEmpty()) {
            throw new EmployeeNotFound("No valid employees found for IDs: " + request.employeeIds());
        }

        List<ShiftAssignments> assignments = new ArrayList<>();

        for (Employee employee : employees) {
            LocalDate newFrom = request.effectiveFrom();
            LocalDate newTo = request.effectiveTo(); // optional

            // 🔹 Step 1: Find latest existing assignment
            Optional<ShiftAssignments> latestAssignmentOpt =
                    shiftAssignmentsRepository.findTopByEmployeeIdOrderByEffectiveToDesc(employee.getId());

            // 🔹 Step 2: Determine effectiveFrom if not given
            if (newFrom == null) {
                if (latestAssignmentOpt.isPresent() && latestAssignmentOpt.get().getEffectiveTo() != null) {
                    newFrom = latestAssignmentOpt.get().getEffectiveTo().plusDays(1);
                } else {
                    newFrom = LocalDate.now(); // fallback if first ever shift or open-ended
                }
            }

            // 🔹 Step 3: Check overlap with any existing assignments
            List<ShiftAssignments> overlappingAssignments =
                    shiftAssignmentsRepository.findOverlappingAssignments(
                            employee.getId(),
                            newFrom,
                            newTo != null ? newTo : newFrom // if no to-date, just check the start
                    );

            if (!overlappingAssignments.isEmpty()) {
                ShiftAssignments existing = overlappingAssignments.get(0);
                throw new EmployeeAreadyExist(
                        "Employee " + employee.getId() +
                                " already has a shift from " + existing.getEffectiveFrom() +
                                " to " + (existing.getEffectiveTo() != null ? existing.getEffectiveTo() : "ongoing") +
                                ". Cannot assign new shift starting " + newFrom + ".");
            }

            // 🔹 Step 4: Adjust previous assignment (close it)
            if (latestAssignmentOpt.isPresent()) {
                ShiftAssignments prev = latestAssignmentOpt.get();
                if (prev.getEffectiveTo() == null || prev.getEffectiveTo().isAfter(newFrom.minusDays(1))) {
                    prev.setEffectiveTo(newFrom.minusDays(1));
                    shiftAssignmentsRepository.save(prev);
                }
            }

            // 🔹 Step 5: Create new assignment
            ShiftAssignments assignment = new ShiftAssignments();
            assignment.setEmployee(employee);
            assignment.setShift(shift);
            assignment.setEffectiveFrom(newFrom);
            assignment.setEffectiveTo(newTo); // can be null (ongoing)
            assignment.setStatus(request.status());
            assignment.setAssignmentSource(request.assignmentSource());
            assignment.setAssignedBy(users.getId());
            assignment.setAssignedAt(LocalDateTime.now());
            assignments.add(assignment);

            employee.setShift(shift);
        }

        // Save all assignments in one batch
        List<ShiftAssignments> savedAssignments = shiftAssignmentsRepository.saveAll(assignments);

        // Save all updated employees in one batch
        employeeRepository.saveAll(employees);

        // Map all to DTOs
        return savedAssignments.stream()
                .map(shiftAssignmentsMapper::toDTO)
                .toList();
    }


  /**  @Override
    public List<ShowShiftAssignDTO> getAllShiftAssignments(ShowShiftAssignRequest shift) {

        Specification<ShiftAssignments> spec =
                ShowShiftSpecification.applicableForShift(shift);

        return shiftAssignmentsRepository.findAll(spec).stream()
                .map(showShiftAssignDTOMapper::toDTO)
                .toList();
    }**/

    @Override
    public List<ShowShiftAssignDTO> getAllShiftAssignments() {

        User users = null;
        String identifier = AuthUtils.getCurrentUsername();

        if (identifier != null && !identifier.isEmpty()) {
            users = userRepository.findByEmailOrPhone(identifier, identifier)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with email/phone: " + identifier));
        } else {
            throw new EntityNotFoundException("No authenticated user found for submission.");
        }

        Long supervisorId = users.getId();

        System.err.println("supervisor id"+supervisorId);

        List<ShiftAssignments> shiftsAssignedBySupervisor = shiftAssignmentsRepository.findByAssignedBy(users.getId());

//        Specification<ShiftAssignments> spec =
//                ShowShiftSpecification.applicableForShift(request,supervisorId);


        if (shiftsAssignedBySupervisor.isEmpty()) {
            System.out.println("No shift assignments found for supervisor ID: " + users.getId());
        }

        return shiftsAssignedBySupervisor.stream()
                .map(showShiftAssignDTOMapper::toDTO)
                .toList();
    }

}
