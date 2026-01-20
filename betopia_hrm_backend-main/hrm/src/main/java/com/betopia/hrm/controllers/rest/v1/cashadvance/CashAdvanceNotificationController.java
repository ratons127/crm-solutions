package com.betopia.hrm.controllers.rest.v1.cashadvance;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.cashadvance.CashAdvanceNotificationDTO;
import com.betopia.hrm.services.cashadvance.CashAdvanceNotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/cashAdvanceNotifications")
@Tag(
        name = "Advance Cash Management -> CashAdvanceNotification dashboard",
        description = "APIs for configurable CashAdvanceNotification. Includes operations to  read, update, CashAdvanceNotification."
)
public class CashAdvanceNotificationController {

    private final CashAdvanceNotificationService cashAdvanceNotificationService;

    public CashAdvanceNotificationController(CashAdvanceNotificationService cashAdvanceNotificationService) {
        this.cashAdvanceNotificationService = cashAdvanceNotificationService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of CashAdvanceNotifications dashboard data",
            description = "Retrieves a paginated list of CashAdvanceNotifications from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch CashAdvanceNotifications records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<CashAdvanceNotificationDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<CashAdvanceNotificationDTO> paginationResponse = cashAdvanceNotificationService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all CashAdvanceNotifications",
            description = "Retrieves a list of all CashAdvanceNotifications available in the system. "
                    + "This endpoint returns the complete CashAdvanceNotifications collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllCashAdvanceApproval()
    {
        List<CashAdvanceNotificationDTO> cashAdvanceNotification = cashAdvanceNotificationService.getAllCashAdvanceNotification();

        GlobalResponse response = GlobalResponse.success(cashAdvanceNotification, "All CashAdvanceNotifications types fetch successful", HttpStatus.OK.value());

        return ResponseEntity.ok(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get CashAdvanceNotifications by ID",
            description = "Retrieves the details of a specific CashAdvanceNotifications using the provided ID. "
                    + "If the CashAdvanceNotifications with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long cashAdvanceNotificationId)
    {
        CashAdvanceNotificationDTO cashAdvanceNotification = cashAdvanceNotificationService.show(cashAdvanceNotificationId);

        GlobalResponse response = GlobalResponse.success(cashAdvanceNotification, "CashAdvanceNotification fetch successful", HttpStatus.OK.value());
        return ResponseEntity.ok(response);
    }

}
