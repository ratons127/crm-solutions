package com.betopia.hrm.controllers.rest.v1.cashadvance;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashConfig;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceSlabConfig;
import com.betopia.hrm.domain.cashadvance.request.AdvanceCashConfigRequest;
import com.betopia.hrm.domain.cashadvance.request.CashAdvanceSlabConfigRequest;
import com.betopia.hrm.domain.cashadvance.request.UpdateCashAdvanceSlabConfigRequest;
import com.betopia.hrm.domain.dto.cashadvance.AdvanceCashConfigDTO;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceSlabConfigDTO;
import com.betopia.hrm.services.cashadvance.AdvanceCashConfigService;
import com.betopia.hrm.services.cashadvance.CashAdvanceSlabConfigService;
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
@RequestMapping("/v1/cashAdvanceSlabConfigs")
@Tag(
        name = "Advance Cash Management -> Cash Advance SlabConfig",
        description = "APIs for configurable Advance Cash. Includes operations to create, read, update, and delete Advance Cash."
)
public class CashAdvanceSlabConfigController {

    private final CashAdvanceSlabConfigService cashAdvanceSlabConfigService;

    public CashAdvanceSlabConfigController(CashAdvanceSlabConfigService cashAdvanceSlabConfigService) {
        this.cashAdvanceSlabConfigService = cashAdvanceSlabConfigService;

    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of CashAdvanceSlabConfig",
            description = "Retrieves a paginated list of AdvanceCashConfig from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch AdvanceCashConfig records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<CashAdvanceSlabConfigDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<CashAdvanceSlabConfigDTO> paginationResponse = cashAdvanceSlabConfigService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all CashAdvanceSlabConfig",
            description = "Retrieves a list of all CashAdvanceSlabConfig available in the system. "
                    + "This endpoint returns the complete CashAdvanceSlabConfig collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllAdvanceCashConfigs()
    {
        List<CashAdvanceSlabConfigDTO> cashAdvanceSlabConfig = cashAdvanceSlabConfigService.getAllCashAdvanceSlabConfig();

        GlobalResponse response = GlobalResponse.success(
                cashAdvanceSlabConfig,
                "All cashAdvanceSlabConfig types fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new CashAdvanceSlabConfig",
            description = "Creates a new CashAdvanceSlabConfig in the system with the provided details. "
                    + "Returns the created CashAdvanceSlabConfig along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody CashAdvanceSlabConfigRequest request)
    {
        CashAdvanceSlabConfigDTO cashAdvanceSlabConfig = cashAdvanceSlabConfigService.store(request);

        GlobalResponse response = GlobalResponse.success(
                cashAdvanceSlabConfig,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get CashAdvanceSlabConfig by ID",
            description = "Retrieves the details of a specific CashAdvanceSlabConfig using the provided ID. "
                    + "If the CashAdvanceSlabConfig with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long cashAdvanceSlabConfigId)
    {
        CashAdvanceSlabConfigDTO cashAdvanceSlabConfig = cashAdvanceSlabConfigService.show(cashAdvanceSlabConfigId);

        GlobalResponse response = GlobalResponse.success(
                cashAdvanceSlabConfig,
                "CashAdvanceSlabConfig fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update CashAdvanceSlabConfig by ID",
            description = "Updates the details of an existing CashAdvanceSlabConfig using the provided ID. "
                    + "If the CashAdvanceSlabConfig with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long cashAdvanceSlabConfigId,
                                                 @Valid @RequestBody UpdateCashAdvanceSlabConfigRequest request)
    {
        CashAdvanceSlabConfigDTO cashAdvanceSlabConfig = cashAdvanceSlabConfigService.update(cashAdvanceSlabConfigId, request);

        GlobalResponse response = GlobalResponse.success(
                cashAdvanceSlabConfig,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete CashAdvanceSlabConfig by ID",
            description = "Deletes a specific CashAdvanceSlabConfig from the system using the provided ID. "
                    + "If the CashAdvanceSlabConfig does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove CashAdvanceSlabConfig records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long cashAdvanceSlabConfigId)
    {
        cashAdvanceSlabConfigService.delete(cashAdvanceSlabConfigId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "CashAdvanceSlabConfig types deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
