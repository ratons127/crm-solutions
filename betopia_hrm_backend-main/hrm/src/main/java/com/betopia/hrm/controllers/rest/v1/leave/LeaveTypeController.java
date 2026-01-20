package com.betopia.hrm.controllers.rest.v1.leave;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.leave.request.LeaveTypeRequest;
import com.betopia.hrm.services.leaves.leavetype.LeaveTypeService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/leave-types")
@Tag(
        name = "Leave Management -> LeaveType",
        description = "Operations related to leave types"
)
public class LeaveTypeController {

    private final LeaveTypeService leaveTypeService ;

    public LeaveTypeController(LeaveTypeService leaveTypeService) {
        this.leaveTypeService = leaveTypeService;
    }

    @GetMapping
    @Operation(summary = "1. Get all leave types with pagination", description = "Retrieve all leave types with pagination")
    public ResponseEntity<PaginationResponse<LeaveType>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<LeaveType> paginationResponse = leaveTypeService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all leave types", description = "Retrieve all leave types without pagination")
    public ResponseEntity<GlobalResponse> getAllLeaveTypes() {
        List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypes();
        return ResponseBuilder.ok(leaveTypes, "All leave types fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get leave type by id", description = "Retrieve a single leave type by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        LeaveType leaveType = leaveTypeService.getLeaveTypeById(id);
        return ResponseBuilder.ok(leaveType, "Leave type fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store leave type", description = "Create/Save a new leave type")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody LeaveTypeRequest request) {
        LeaveType createdLeaveType = leaveTypeService.store(request);
        return ResponseBuilder.created(createdLeaveType, "Leave type created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update leave type", description = "Update an existing leave type")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody LeaveTypeRequest request) {
        LeaveType updatedLeaveType = leaveTypeService.updateLeaveType(id, request);
        return ResponseBuilder.ok(updatedLeaveType, "Leave type updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete leave type", description = "Remove a leave type from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        leaveTypeService.deleteLeaveType(id);
        return ResponseBuilder.noContent("Leave type deleted successfully");
    }

    @PostMapping("/import")
    public ResponseEntity<?> importFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<LeaveType> imported = leaveTypeService.importAndSave(file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", imported.size() + " leave categories imported successfully",
                    "count", imported.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "Import failed: " + e.getMessage()
            ));
        }
    }

    /**
     * true = active only, false = all
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCategories(
            @RequestParam(defaultValue = "true") boolean type,
            @RequestParam(defaultValue = "excel") String format
    ) {
        try {
            byte[] data = leaveTypeService.export(type, format);

            HttpHeaders headers = new HttpHeaders();

            // Set content type
            if ("csv".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.parseMediaType("text/csv"));
            } else if ("pdf".equalsIgnoreCase(format)) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            } else { // default Excel
                headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }

            // Set filename
            String filename = (type ? "all-" : "") + "leave-types." +
                    (format.equalsIgnoreCase("excel") ? "xlsx" : format.toLowerCase());
            headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/active")
    @Operation(summary = "Get all active leave types",
            description = "Returns all leave types that are currently active (status = true)")
    public ResponseEntity<List<LeaveType>> getActiveLeaveTypes() {
        List<LeaveType> leaveTypes = leaveTypeService.getAllLeaveTypeByStatus(true);

        if (leaveTypes.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.emptyList());
        }

        return ResponseEntity.ok(leaveTypes);
    }


}
