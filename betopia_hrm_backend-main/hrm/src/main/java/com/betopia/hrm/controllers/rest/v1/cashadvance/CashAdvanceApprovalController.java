package com.betopia.hrm.controllers.rest.v1.cashadvance;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.cashadvance.entity.CashAdvanceApproval;
import com.betopia.hrm.domain.cashadvance.request.CashAdvanceApprovalRequest;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceApprovalDTO;
import com.betopia.hrm.services.cashadvance.CashAdvanceApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/cashAdvanceApprovals")
@Tag(
        name = "Advance Cash Management -> Cash Advance Approval",
        description = "APIs for configurable Cash Advance Approval. Includes operations to  read, update, Cash Advance Approval."
)
public class CashAdvanceApprovalController {

    private final CashAdvanceApprovalService cashAdvanceApprovalService;

    public CashAdvanceApprovalController(CashAdvanceApprovalService cashAdvanceApprovalService) {
        this.cashAdvanceApprovalService = cashAdvanceApprovalService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of CashAdvanceApprovals dashboard data",
            description = "Retrieves a paginated list of CashAdvanceApprovals from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch CashAdvanceApprovals records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<CashAdvanceApprovalDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<CashAdvanceApprovalDTO> paginationResponse = cashAdvanceApprovalService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all CashAdvanceApproval",
            description = "Retrieves a list of all CashAdvanceApproval available in the system. "
                    + "This endpoint returns the complete CashAdvanceApproval collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllCashAdvanceApproval()
    {
        List<CashAdvanceApprovalDTO> cashAdvanceApproval = cashAdvanceApprovalService.getAllCashAdvanceApproval();

        GlobalResponse response = GlobalResponse.success(cashAdvanceApproval, "All CashAdvanceApproval types fetch successful", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get CashAdvanceApproval by ID",
            description = "Retrieves the details of a specific CashAdvanceApproval using the provided ID. "
                    + "If the CashAdvanceApproval with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long cashAdvanceApprovalId)
    {
        CashAdvanceApprovalDTO cashAdvanceApproval = cashAdvanceApprovalService.show(cashAdvanceApprovalId);

        GlobalResponse response = GlobalResponse.success(
                cashAdvanceApproval,
                "AdvanceCashRequest fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PatchMapping("{id}")
    @Operation(
            summary = "5. Update CashAdvanceApproval by ID",
            description = "Updates the details of an existing CashAdvanceApproval using the provided ID. "
                    + "If the CashAdvanceApproval with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long cashAdvanceApprovalId, @Valid @RequestBody CashAdvanceApprovalRequest request)
    {
        CashAdvanceApprovalDTO cashAdvanceApproval = cashAdvanceApprovalService.updateStatus(cashAdvanceApprovalId, request);

        GlobalResponse response = GlobalResponse.success(cashAdvanceApproval, "Update successful", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

}
