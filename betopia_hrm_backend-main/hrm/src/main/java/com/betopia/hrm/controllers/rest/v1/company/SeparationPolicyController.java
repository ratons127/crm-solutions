package com.betopia.hrm.controllers.rest.v1.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.SeparationPolicy;
import com.betopia.hrm.domain.company.request.SeparationPolicyRequest;
import com.betopia.hrm.domain.dto.company.SeparationPolicyDTO;
import com.betopia.hrm.services.company.Separationpolicy.SeparationPolicyService;
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
@RequestMapping("/v1/separation-policies")
@Tag(
        name = "Company -> Separation policy",
        description = "APIs for managing Separation policy. Includes operations to create, read, update, and delete Separation policy information."
)
public class SeparationPolicyController {

    private final SeparationPolicyService separationPolicyService;

    public SeparationPolicyController(SeparationPolicyService separationPolicyService) {
        this.separationPolicyService = separationPolicyService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of Separation policies",
            description = "Retrieves a paginated list of Separation policies from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch Separation policy records in a paginated format instead of retrieving all at once."
    )
//    @PreAuthorize("hasAuthority('separation-policies-list')")
    public ResponseEntity<PaginationResponse<SeparationPolicy>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<SeparationPolicy> paginationResponse = separationPolicyService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all separation policies by companyId",
            description = "Retrieves a list of all separation policies available in the system. "
                    + "This endpoint returns the complete separation policy collection without pagination. "
                    + "Use it when you need to fetch all separation policies at once."
    )
//    @PreAuthorize("hasAuthority('separation-policies-list')")
    public ResponseEntity<GlobalResponse> getAllSeparationPolicies()
    {
        List<SeparationPolicyDTO> separationPolicies = separationPolicyService.getAllSeparationPolicies();
        GlobalResponse response = GlobalResponse.success(
                separationPolicies,
                "All Separation Policies fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/company/{companyId}")
    @Operation(
            summary = "2. Get all separation policies by companyId",
            description = "Retrieves a list of all separation policies by companyId available in the system. "
                    + "This endpoint returns the complete separation policy collection without pagination. "
                    + "Use it when you need to fetch all separation policies at once."
    )
    public ResponseEntity<GlobalResponse> getSeparationPoliciesByCompanyId(@PathVariable Long companyId)
    {
        List<SeparationPolicyDTO> separationPolicies = separationPolicyService.getSeparationPoliciesByCompanyId(companyId);
        GlobalResponse response = GlobalResponse.success(
                separationPolicies,
                "All Separation Policies By company Id fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }


    @PostMapping
    @Operation(
            summary = "3. Create a new separation policy",
            description = "Creates a new separation policy in the system with the provided details. "
                    + "Required fields such as separation policy name, company id,workplace id , and effective_from information must be included in the request body. "
                    + "Returns the created separation policy information along with its unique ID."
    )
//    @PreAuthorize("hasAuthority('separation-policy-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody SeparationPolicyRequest request)
    {
        SeparationPolicyDTO separationPolicy = separationPolicyService.store(request);
        GlobalResponse response = GlobalResponse.success(
                separationPolicy,
                "Store successfully",
                HttpStatus.CREATED.value()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get separation policy by ID",
            description = "Retrieves the details of a specific separation policy using the provided ID. "
                    + "If the separation policy with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('separation-policy-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long separationPolicyId)
    {
        SeparationPolicyDTO separationPolicy = separationPolicyService.show(separationPolicyId);
        GlobalResponse response = GlobalResponse.success(
                separationPolicy,
                "separation Policy fetch successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update separation policy by ID",
            description = "Updates the details of an existing separation policy using the provided ID. "
                    + "This endpoint allows modifying separation policy information such as name, location, or other attributes. "
                    + "If the separation policy with the given ID does not exist, a 404 Not Found response will be returned."
    )
//    @PreAuthorize("hasAuthority('separation-policy-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long separationPolicyId,
                                                 @Valid @RequestBody SeparationPolicyRequest request)
    {
        SeparationPolicyDTO separationPolicy = separationPolicyService.update(separationPolicyId, request);
        GlobalResponse response = GlobalResponse.success(
                separationPolicy,
                "Update successful",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete separation policy by ID",
            description = "Deletes a specific separation policy from the system using the provided ID. "
                    + "If the separation policy does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove separation policy records."
    )
//    @PreAuthorize("hasAuthority('separation-policy-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long separationPolicyId)
    {
        separationPolicyService.destroy(separationPolicyId);
        GlobalResponse response = GlobalResponse.success(
                null,
                "separation policy deleted successfully",
                HttpStatus.OK.value()
        );
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    //    @PreAuthorize("hasAuthority('separation-policy-edit')")
    @PatchMapping("{id}")
    public ResponseEntity<GlobalResponse> updateSeparationPolicyStatus(
            @PathVariable("id") Long separationPolicyId,
            @RequestBody SeparationPolicyRequest request
    )  {
        SeparationPolicyDTO separationPolicy = separationPolicyService.updateSeparationPolicyStatus(separationPolicyId, request);
        GlobalResponse response = GlobalResponse.success(
                separationPolicy,
                "Separation policy Status change",
                HttpStatus.OK.value()
        );
        return ResponseEntity.ok(response);
    }
}
