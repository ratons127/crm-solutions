package com.betopia.hrm.controllers.rest.v1;

import com.betopia.hrm.domain.users.entity.Company;
import com.betopia.hrm.domain.users.request.CompanyRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.users.company.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.time.ZoneId;
import java.util.Currency;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/companies")
@Tag(name = "User Management -> Company", description = "Operation related to managing companies")
public class CompanyController {

    private final CompanyService companyService;


    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping
    @Operation(summary = "1. Get all companies with pagination", description = "Retrieve all companies from database using pagination")
    @PreAuthorize("hasAuthority('company-list')")
    public ResponseEntity<PaginationResponse<Company>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Company> paginationResponse = companyService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all companies", description = "Retrieve all companies from database")
//    @PreAuthorize("hasAuthority('company-list')")
    public ResponseEntity<GlobalResponse> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies();

        GlobalResponse response = GlobalResponse.success(
                companies,
                "All companies fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "3. Store companies", description = "Creating a new companies into the system")
//    @PreAuthorize("hasAuthority('company-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody CompanyRequest request)
    {
        Company company = companyService.store(request);

        GlobalResponse response = GlobalResponse.success(
                company,
                "Store successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "4. Get by id companies", description = "Companies get from database using by id")
//    @PreAuthorize("hasAuthority('company-edit')")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long companyId)
    {
        Company company = companyService.show(companyId);

        GlobalResponse response = GlobalResponse.success(
                company,
                "Company fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "5. Update companies", description = "Update companies into the system")
//    @PreAuthorize("hasAuthority('company-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long companyId, @Valid @RequestBody CompanyRequest request)
    {
        Company company = companyService.update(companyId, request);

        GlobalResponse response = GlobalResponse.success(
                company,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Delete resource by ID",
            description = "Deletes a specific resource from the system based on the provided ID. "
                    + "If the resource with the given ID does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove a record from the database.")
//    @PreAuthorize("hasAuthority('company-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long companyId)
    {
        companyService.destroy(companyId);

        GlobalResponse response = GlobalResponse.success(
                "",
                "Delete successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

@PostMapping(value = "/{id}/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
@Operation(summary = "Upload companies image", description = "Upload company image to S3 and store reference in DB")
@PreAuthorize("hasAuthority('company-images-update')")
public ResponseEntity<GlobalResponse> uploadCompanyImage(
        @PathVariable("id") Long companyId,
        @RequestParam("file") MultipartFile file
) {
    return companyService.uploadCompanyImage(companyId, file);
}

    @DeleteMapping("{id}/delete-image")
    @Operation(summary = "Delete company image", description = "Delete company image from S3 and remove reference in DB")
    @PreAuthorize("hasAuthority('company-image-delete')")
    public ResponseEntity<GlobalResponse> deleteCompanyImage(@PathVariable("id") Long companyId) {
        return companyService.deleteCompanyImage(companyId);
    }

    @GetMapping("all-currencies")
    @Operation(summary = "Get all currencies", description = "Retrieve all currencies")
//    @PreAuthorize("hasAuthority('currencies-list')")
    public List<Currency> getAllCurrencies() {
        return companyService.getAllCurrencies();
    }

    @GetMapping("all-zones")
    @Operation(summary = "Get all timezones", description = "Retrieve all time zones")
//    @PreAuthorize("hasAuthority('zone-list')")
    public List<ZoneId> getAllTimeZone() {
        return companyService.getAllTimeZone();
    }
}
