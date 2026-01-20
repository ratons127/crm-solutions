package com.betopia.hrm.controllers.rest.v1.leave;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.leave.entity.LeaveCategory;
import com.betopia.hrm.domain.leave.entity.LeaveGroupAssign;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.leave.request.LeaveCategoryRequest;
import com.betopia.hrm.services.base.HrmFileService;
import com.betopia.hrm.services.leaves.leaveCategory.LeaveCategoryService;
import com.betopia.hrm.webapp.util.CommonDTO;
import com.betopia.hrm.webapp.util.GenericMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/leave-categories")
@Tag(
        name = "Leave Management -> Leave Categories",
        description = "APIs for managing leave categories"
)
public class LeaveCategoryController {

    private final LeaveCategoryService leaveCategoryService;

    private final GenericMapper genericMapper;

    public LeaveCategoryController(LeaveCategoryService leaveCategoryService, GenericMapper genericMapper) {
        this.leaveCategoryService = leaveCategoryService;
        this.genericMapper = genericMapper;
    }

    @GetMapping
    @Operation(
            summary = "Get All Leave Categories",
            description = "Fetch a list of all leave categories"
    )
    public ResponseEntity<?> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<LeaveCategory> paginationResponse = leaveCategoryService.index(direction, page, perPage);

        List<Map<String, Object>> dtoList = paginationResponse.getData().stream()
                .map(genericMapper::toDTO)  // CommonDTO
                .map(CommonDTO::toMap)      // Map<String, Object>, extra "fields" gone
                .collect(Collectors.toList());

        PaginationResponse<Map<String, Object>> dtoPagination = new PaginationResponse<>();
        dtoPagination.setData(dtoList);
        dtoPagination.setLinks(paginationResponse.getLinks());
        dtoPagination.setMeta(paginationResponse.getMeta());
        dtoPagination.setSuccess(paginationResponse.isSuccess());
        dtoPagination.setStatusCode(paginationResponse.getStatusCode());
        dtoPagination.setMessage(paginationResponse.getMessage());

        return ResponseEntity.ok(dtoPagination);
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get All Leave Categories Without Pagination",
            description = "Fetch a list of all leave categories without pagination"
    )
    public ResponseEntity<GlobalResponse> getAllLeaveCategories() {
        List<LeaveCategory> leaveCategories = leaveCategoryService.getAll();
        List<Map<String, Object>> dtoList = leaveCategories.stream()
                .map(genericMapper::toDTO)   // CommonDTO
                .map(CommonDTO::toMap)       // Map<String, Object> for JSON
                .collect(Collectors.toList());
        return ResponseEntity.ok(GlobalResponse.success(dtoList, "All leave categories fetched", 200));
    }

    @PostMapping
    @Operation(
            summary = "Create a New Leave Category",
            description = "Create a new leave category with the provided details"
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody LeaveCategoryRequest request) {
        var leaveCategory = leaveCategoryService.store(request);
        CommonDTO responseDTO = genericMapper.toDTO(leaveCategory);
        return ResponseEntity.ok(GlobalResponse.success(responseDTO.toMap(), "Leave category created successfully", 201));
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Get Leave Category by ID",
            description = "Fetch a specific leave category by its ID"
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable Long id) {
        var leaveCategory = leaveCategoryService.show(id);
        CommonDTO responseDTO = genericMapper.toDTO(leaveCategory);
        return ResponseEntity.ok(GlobalResponse.success(responseDTO.toMap(), "Leave category fetched successfully", 200));
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Update Leave Category",
            description = "Update an existing leave category with the provided details"
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable Long id, @Valid @RequestBody LeaveCategoryRequest request) {
        var leaveCategory = leaveCategoryService.update(id, request);
        CommonDTO responseDTO = genericMapper.toDTO(leaveCategory);
        return ResponseEntity.ok(GlobalResponse.success(responseDTO.toMap(), "Leave category updated successfully", 200));
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Delete Leave Category",
            description = "Delete a specific leave category by its ID"
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable Long id) {
        leaveCategoryService.destroy(id);
        return ResponseEntity.ok(GlobalResponse.success(null, "Leave category deleted successfully", 200));
    }

    /**
     * Import Leave Categories from Excel
     * POST /api/leave-categories/import
     */
    @PostMapping("/import")
    public ResponseEntity<?> importFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<LeaveCategory> imported = leaveCategoryService.importAndSave(file);
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

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCategories(
            @RequestParam(defaultValue = "false") boolean all,   // false = active only, true = all
            @RequestParam(defaultValue = "excel") String format // "excel", "csv", "pdf"
    ) {
        try {
            byte[] data = leaveCategoryService.export(all, format);

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
            String filename = (all ? "all-" : "") + "leave-categories." +
                    (format.equalsIgnoreCase("excel") ? "xlsx" : format.toLowerCase());
            headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());

            return new ResponseEntity<>(data, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}
