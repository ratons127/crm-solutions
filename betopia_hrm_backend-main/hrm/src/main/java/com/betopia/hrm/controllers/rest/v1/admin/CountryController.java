package com.betopia.hrm.controllers.rest.v1.admin;

import com.betopia.hrm.domain.admin.entity.Country;
import com.betopia.hrm.domain.admin.request.CountryRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.admin.CountryService;
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
@RequestMapping("/v1/countries")
@Tag(
        name = "User Management -> Country setup",
        description = "APIs for configurable country. Includes operations to create, read, update, and delete country."
)
public class CountryController {

    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of country",
            description = "Retrieves a paginated list of country from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch country records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<Country>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Country> paginationResponse = countryService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all country",
            description = "Retrieves a list of all country available in the system. "
                    + "This endpoint returns the complete country collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllPasswordPolicy()
    {
        List<Country> country = countryService.getAllCountries();

        GlobalResponse response = GlobalResponse.success(
                country,
                "All country fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new country",
            description = "Creates a new country in the system with the provided details. "
                    + "Required fields such as code,name must be included in the request body. "
                    + "Returns the created country along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody CountryRequest request)
    {
        Country country = countryService.insert(request);

        GlobalResponse response = GlobalResponse.success(
                country,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get country by ID",
            description = "Retrieves the details of a specific country using the provided ID. "
                    + "If the country with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long countryId)
    {
        Country country = countryService.show(countryId);

        GlobalResponse response = GlobalResponse.success(
                country,
                "Country fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update country by ID",
            description = "Updates the details of an existing country using the provided ID. "
                    + "This endpoint allows modifying country code,name,region attributes. "
                    + "If the country with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long countryId,
                                                 @Valid @RequestBody CountryRequest request)
    {
        Country country = countryService.update(countryId, request);

        GlobalResponse response = GlobalResponse.success(
                country,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete country by ID",
            description = "Deletes a specific country from the system using the provided ID. "
                    + "If the country does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove country records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long countryId)
    {
        countryService.delete(countryId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Country deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
