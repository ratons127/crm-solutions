package com.betopia.hrm.controllers.rest.v1;

import com.betopia.hrm.domain.users.entity.PasswordPolicy;
import com.betopia.hrm.domain.users.repository.PasswordPolicyRepository;
import com.betopia.hrm.domain.users.request.PasswordPolicyRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.users.user.PasswordPolicyService;
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
@RequestMapping("/v1/passwordPolicies")
@Tag(
        name = "User Management -> Password Policy setup",
        description = "APIs for configurable password policy. Includes operations to create, read, update, and delete password policy."
)
public class PasswordPolicyController {

    private final PasswordPolicyService passwordPolicyService;

    public PasswordPolicyController(PasswordPolicyService passwordPolicyService) {
        this.passwordPolicyService = passwordPolicyService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of password policies",
            description = "Retrieves a paginated list of password policies from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch password policy records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<PasswordPolicy>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<PasswordPolicy> paginationResponse = passwordPolicyService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all password policy",
            description = "Retrieves a list of all password policy available in the system. "
                    + "This endpoint returns the complete password policy collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllPasswordPolicy()
    {
        List<PasswordPolicy> passwordPolicies = passwordPolicyService.getAllPasswordPolicies();

        GlobalResponse response = GlobalResponse.success(
                passwordPolicies,
                "All password policy fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new password policy",
            description = "Creates a new password policy in the system with the provided details. "
                    + "Required fields such as minLength,maxLength must be included in the request body. "
                    + "Returns the created password policy along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody PasswordPolicyRequest request)
    {
        PasswordPolicy passwordPolicy = passwordPolicyService.insert(request);

        GlobalResponse response = GlobalResponse.success(
                passwordPolicy,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get password policy by ID",
            description = "Retrieves the details of a specific password policy using the provided ID. "
                    + "If the password policy with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long passwordPolicyId)
    {
        PasswordPolicy passwordPolicy = passwordPolicyService.show(passwordPolicyId);

        GlobalResponse response = GlobalResponse.success(
                passwordPolicy,
                "Password policy fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update password policy by ID",
            description = "Updates the details of an existing password policy using the provided ID. "
                    + "This endpoint allows modifying password policy minLength,maxLength or other attributes. "
                    + "If the password policy with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long passwordPolicyId,
                                                 @Valid @RequestBody PasswordPolicyRequest request)
    {
        PasswordPolicy passwordPolicy = passwordPolicyService.update(passwordPolicyId, request);

        GlobalResponse response = GlobalResponse.success(
                passwordPolicy,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete password policy by ID",
            description = "Deletes a specific password policy from the system using the provided ID. "
                    + "If the password policy does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove password policy records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long passwordPolicyId)
    {
        passwordPolicyService.delete(passwordPolicyId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Password policy deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
