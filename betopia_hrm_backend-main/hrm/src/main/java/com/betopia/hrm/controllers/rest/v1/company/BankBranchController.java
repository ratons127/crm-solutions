package com.betopia.hrm.controllers.rest.v1.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.BankBranch;
import com.betopia.hrm.domain.company.request.BankBranchRequest;
import com.betopia.hrm.services.company.bankbranch.BankBranchService;
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
@RequestMapping("/v1/bank-branches")
@Tag(
        name = "Company -> Bank branch",
        description = "APIs for managing branch. Includes operations to create, read, update, and delete branch information."
)
public class BankBranchController {

    private final BankBranchService bankBranchService;
    public BankBranchController(BankBranchService bankBranchService) {
        this.bankBranchService = bankBranchService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of bank branches",
            description = "Retrieves a paginated list of bank branches from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch bank branches records in a paginated format instead of retrieving all at once."
    )
    //@PreAuthorize("hasAuthority('bank-branch-list')")
    public ResponseEntity<PaginationResponse<BankBranch>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<BankBranch> paginationResponse = bankBranchService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all bank branches",
            description = "Retrieves a list of all bank branches available in the system. "
                    + "This endpoint returns the completebank branch collection without pagination. "
                    + "Use it when you need to fetch all bank branches at once."
    )
//    @PreAuthorize("hasAuthority('bank-branch-list')")
    public ResponseEntity<GlobalResponse> getAllBankBranches()
    {
        List<BankBranch> bankBranches = bankBranchService.getAllBankBranches();
        GlobalResponse response = GlobalResponse.success(
                bankBranches,
                "All Bank branches fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new bank branch",
            description = "Creates a new bank branch in the system with the provided details. "
                    + "Required fields such as bank branch name, address, and contact information must be included in the request body. "
                    + "Returns the created bank branch information along with its unique ID."
    )
//    @PreAuthorize("hasAuthority('bank-branch-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody BankBranchRequest request)
    {
        BankBranch bankBranch = bankBranchService.store(request);
        GlobalResponse response = GlobalResponse.success(
                bankBranch,
                "Store successfully",
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get bank branch by ID",
            description = "Retrieves the details of a specific bank branch using the provided ID. "
                    + "If the bank branch with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('bank-branch-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long bankBranchId)
    {
        BankBranch bankBranch = bankBranchService.show(bankBranchId);
        GlobalResponse response = GlobalResponse.success(
                bankBranch,
                "Bank branch fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update bank branch by ID",
            description = "Updates the details of an existing bank branch using the provided ID. "
                    + "This endpoint allows modifying bank branche information such as name, location, or other attributes. "
                    + "If the bank branch with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('bank-branch-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long bankBranchId,
                                                 @Valid @RequestBody BankBranchRequest request)
    {
        BankBranch bankBranch = bankBranchService.update(bankBranchId, request);
        GlobalResponse response = GlobalResponse.success(
                bankBranch,
                "Update successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete bank branch by ID",
            description = "Deletes a specific bank branch from the system using the provided ID. "
                    + "If the bank branch does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove bank branch records."
    )
//    @PreAuthorize("hasAuthority('bank-branch-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long bankBranchId)
    {
        bankBranchService.destroy(bankBranchId);
        GlobalResponse response = GlobalResponse.success(
                null,
                "Bank branch deleted successfully",
                HttpStatus.OK.value()
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
