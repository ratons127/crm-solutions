package com.betopia.hrm.controllers.rest.v1.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Bank;
import com.betopia.hrm.domain.company.request.BankRequest;
import com.betopia.hrm.services.company.bank.BankService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/bank")
@Tag(
        name = "Company -> Bank",
        description = "APIs for managing Bank. Includes operations to create, read, update, and delete bank information."
)
public class BankController {

    private final BankService bankService;

    public BankController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of banks",
            description = "Retrieves a paginated list of banks from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch bank records in a paginated format instead of retrieving all at once."
    )
//    @PreAuthorize("hasAuthority('bank-list')")
    public ResponseEntity<PaginationResponse<Bank>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<Bank> paginationResponse = bankService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all banks",
            description = "Retrieves a list of all banks available in the system. "
                    + "This endpoint returns the complete bank collection without pagination. "
                    + "Use it when you need to fetch all banks at once."
    )
//    @PreAuthorize("hasAuthority('bank-list')")
    public ResponseEntity<GlobalResponse> getAllBanks()
    {
        List<Bank> banks = bankService.getAllBanks();
        GlobalResponse response = GlobalResponse.success(
                banks,
                "All Banks fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new bank",
            description = "Creates a new bank in the system with the provided details. "
                    + "Required fields such as bank name, address, and contact information must be included in the request body. "
                    + "Returns the created bank information along with its unique ID."
    )
//    @PreAuthorize("hasAuthority('bank-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody BankRequest request)
    {
        Bank bank = bankService.store(request);
        GlobalResponse response = GlobalResponse.success(
                bank,
                "Store successfully",
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get bank by ID",
            description = "Retrieves the details of a specific bank using the provided ID. "
                    + "If the bank with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('bank-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long bankId)
    {
        Bank bank = bankService.show(bankId);
        GlobalResponse response = GlobalResponse.success(
                bank,
                "Bank fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update bank by ID",
            description = "Updates the details of an existing bank using the provided ID. "
                    + "This endpoint allows modifying bank information such as name, location, or other attributes. "
                    + "If the bank with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('bank-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long bankId,
                                                 @Valid @RequestBody BankRequest request)
    {
        Bank bank = bankService.update(bankId, request);
        GlobalResponse response = GlobalResponse.success(
                bank,
                "Update successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete bank Unit by ID",
            description = "Deletes a specific bank from the system using the provided ID. "
                    + "If the bank does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove bank records."
    )
//    @PreAuthorize("hasAuthority('bank-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long bankId)
    {
        bankService.destroy(bankId);
        GlobalResponse response = GlobalResponse.success(
                null,
                "Bank deleted successfully",
                HttpStatus.OK.value()
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
