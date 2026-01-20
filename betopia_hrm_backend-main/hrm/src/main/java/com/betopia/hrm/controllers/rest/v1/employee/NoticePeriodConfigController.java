package com.betopia.hrm.controllers.rest.v1.employee;


import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.employee.NoticePeriodConfigDTO;
import com.betopia.hrm.domain.employee.request.NoticePeriodConfigRequest;
import com.betopia.hrm.services.employee.noticeperiodconfig.NoticePeriodConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/notice-period")
@Tag(
        name = "Employee Management -> Notice Period Config",
        description = "APIs for configurable notice period. Includes operations to create, read, update, and delete notice period."
)
public class NoticePeriodConfigController {

    private final NoticePeriodConfigService noticePeriodConfigService;

    public NoticePeriodConfigController(NoticePeriodConfigService noticePeriodConfigService) {
        this.noticePeriodConfigService = noticePeriodConfigService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get all notice period with pagination",
            description = "Retrieve a list of all notice period with pagination."
    )
    public ResponseEntity<PaginationResponse<NoticePeriodConfigDTO>> getAllShifts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage,
            @RequestParam(defaultValue = "ASC") String sortDirection) {

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        PaginationResponse<NoticePeriodConfigDTO> response = noticePeriodConfigService.index(direction, page, perPage);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all notice period without pagination",
            description = "Retrieve a list of all notice period without pagination"
    )
    public ResponseEntity<GlobalResponse> getAllShift()
    {
        List<NoticePeriodConfigDTO> data = noticePeriodConfigService.getAllNoticePeriodConfigs();

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    @Operation(
            summary = "3. Store notice period",
            description = "Creating a new notice period"
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody NoticePeriodConfigRequest request)
    {
        var data = noticePeriodConfigService.store(request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Store successful",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED.value()).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "3. show notice period by id",
            description = "Show a new notice period"
    )
    // @PreAuthorize("hasAuthority('shift-edit')
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id)
    {
        var data = noticePeriodConfigService.show(id);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/update/{id}")
    @Operation(
            summary = "3. Update notice period",
            description = "Updating a new notice period"
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long id, @Valid @RequestBody NoticePeriodConfigRequest request)
    {
        var data = noticePeriodConfigService.update(id, request);

        GlobalResponse response = GlobalResponse.success(
                data,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "3. Delete notice period",
            description = "Deleting a new notice period"
    )
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long shiftId)
    {
        noticePeriodConfigService.destroy(shiftId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.status(HttpStatus.OK.value()).body(response);
    }
}
