package com.betopia.hrm.controllers.rest.v1.cashadvance;


import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashConfig;
import com.betopia.hrm.domain.cashadvance.request.AdvanceCashConfigRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.cashadvance.AdvanceCashConfigDTO;
import com.betopia.hrm.services.cashadvance.AdvanceCashConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/advance-cash-configs")
@Tag(
        name = "Advance Cash Management -> Advance cash setup",
        description = "APIs for configurable Advance Cash. Includes operations to create, read, update, and delete Advance Cash."
)
public class AdvanceCashConfigController {

    private final AdvanceCashConfigService advanceCashConfigService;

    public AdvanceCashConfigController(AdvanceCashConfigService advanceCashConfigService) {
        this.advanceCashConfigService = advanceCashConfigService;
    }
    @GetMapping
    @Operation(
            summary = "1. Get paginated list of AdvanceCashConfig",
            description = "Retrieves a paginated list of AdvanceCashConfig from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch AdvanceCashConfig records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<AdvanceCashConfigDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<AdvanceCashConfigDTO> paginationResponse = advanceCashConfigService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all AdvanceCashConfig",
            description = "Retrieves a list of all AdvanceCashConfig available in the system. "
                    + "This endpoint returns the complete AdvanceCashConfig collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllAdvanceCashConfigs()
    {
        List<AdvanceCashConfigDTO> advanceCashConfigDTO = advanceCashConfigService.getAllAdvanceCashConfig();

        GlobalResponse response = GlobalResponse.success(
                advanceCashConfigDTO,
                "All advanceCashConfigs types fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new AdvanceCashConfig",
            description = "Creates a new AdvanceCashConfig in the system with the provided details. "
                    + "Returns the created AdvanceCashConfig along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody AdvanceCashConfigRequest request)
    {
        AdvanceCashConfigDTO advanceCashConfig = advanceCashConfigService.store(request);

        GlobalResponse response = GlobalResponse.success(
                advanceCashConfig,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get AdvanceCashConfig by ID",
            description = "Retrieves the details of a specific AdvanceCashConfig using the provided ID. "
                    + "If the AdvanceCashConfig with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long advanceCashConfigId)
    {
        AdvanceCashConfigDTO advanceCashConfig = advanceCashConfigService.show(advanceCashConfigId);

        GlobalResponse response = GlobalResponse.success(
                advanceCashConfig,
                "AdvanceCashConfig fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update AdvanceCashConfig by ID",
            description = "Updates the details of an existing AdvanceCashConfig using the provided ID. "
                    + "If the AdvanceCashConfig with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long advanceCashConfigId,
                                                 @Valid @RequestBody AdvanceCashConfigRequest request)
    {
        AdvanceCashConfigDTO advanceCashConfig = advanceCashConfigService.update(advanceCashConfigId, request);

        GlobalResponse response = GlobalResponse.success(
                advanceCashConfig,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete AdvanceCashConfig by ID",
            description = "Deletes a specific AdvanceCashConfig from the system using the provided ID. "
                    + "If the AdvanceCashConfig does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove AdvanceCashConfig records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long advanceCashConfigId)
    {
        advanceCashConfigService.delete(advanceCashConfigId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "AdvanceCashConfig types deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

}

