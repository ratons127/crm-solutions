package com.betopia.hrm.controllers.rest.v1.cashadvance;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.AdvanceCashRequest;
import com.betopia.hrm.domain.cashadvance.model.AdvanceRequestDTO;
import com.betopia.hrm.domain.cashadvance.request.AdvanceCashRequestRequest;
import com.betopia.hrm.domain.dto.cashadvance.AdvanceCashRequestDTO;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceSlabConfigDTO;
import com.betopia.hrm.services.cashadvance.AdvanceCashRequestService;
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

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/advance-cash-request")
@Tag(
        name = "Advance Cash Management -> Advance cash request entry",
        description = "APIs for configurable Advance Cash Request. Includes operations to create, read, update, and delete Advance Cash Request."
)
public class AdvanceCashRequestController {

    private final AdvanceCashRequestService advanceCashRequestService;

    public AdvanceCashRequestController(AdvanceCashRequestService advanceCashRequestService) {
        this.advanceCashRequestService = advanceCashRequestService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of AdvanceCashRequest",
            description = "Retrieves a paginated list of AdvanceCashRequest from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch AdvanceCashRequest records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<AdvanceCashRequestDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<AdvanceCashRequestDTO> paginationResponse = advanceCashRequestService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all AdvanceCashRequest",
            description = "Retrieves a list of all AdvanceCashRequest available in the system. "
                    + "This endpoint returns the complete AdvanceCashRequest collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllAdvanceCashRequest()
    {
        List<AdvanceCashRequestDTO> advanceCashRequest = advanceCashRequestService.getAllAdvanceCashRequest();

        GlobalResponse response = GlobalResponse.success(
                advanceCashRequest,
                "All advanceCashRequest types fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new AdvanceCashRequest",
            description = "Creates a new AdvanceCashRequest in the system with the provided details. "
                    + "Returns the created AdvanceCashRequest along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody AdvanceCashRequestRequest request)
    {
        AdvanceCashRequestDTO advanceCashRequest = advanceCashRequestService.store(request);

        GlobalResponse response = GlobalResponse.success(
                advanceCashRequest,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get AdvanceCashRequest by ID",
            description = "Retrieves the details of a specific AdvanceCashRequest using the provided ID. "
                    + "If the AdvanceCashRequest with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long advanceCashRequestId)
    {
        AdvanceCashRequestDTO advanceCashRequest = advanceCashRequestService.show(advanceCashRequestId);

        GlobalResponse response = GlobalResponse.success(
                advanceCashRequest,
                "AdvanceCashRequest fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update AdvanceCashRequest by ID",
            description = "Updates the details of an existing AdvanceCashRequest using the provided ID. "
                    + "If the AdvanceCashRequest with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long advanceCashRequestId,
                                                 @Valid @RequestBody AdvanceCashRequestRequest request)
    {
        AdvanceCashRequestDTO advanceCashRequest = advanceCashRequestService.update(advanceCashRequestId, request);

        GlobalResponse response = GlobalResponse.success(
                advanceCashRequest,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete AdvanceCashRequest by ID",
            description = "Deletes a specific AdvanceCashRequest from the system using the provided ID. "
                    + "If the AdvanceCashRequest does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove AdvanceCashRequest records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long advanceCashRequestId)
    {
        advanceCashRequestService.delete(advanceCashRequestId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "AdvanceCashRequest types deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping("/cashAdvanceSlabConfig/{requestAmt}")
    @Operation(
            summary = "4. Get CashAdvanceSlabConfig ",
            description = "Retrieves the details of a specific CashAdvanceSlabConfig using login employee and current date"
                    + "If the CashAdvanceSlabConfig with the given login employee does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> getCashAdvanceSlabConfigByLoginEmployee(@PathVariable("requestAmt") BigDecimal requestAmt)
    {
        AdvanceRequestDTO cshAdvanceSlabConfig = advanceCashRequestService.getAdvanceCashRequestByEmployee(requestAmt);

        GlobalResponse response = GlobalResponse.success(
                cshAdvanceSlabConfig,
                "CashAdvanceSlabConfig fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }
}
