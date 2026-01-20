package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.InstituteName;
import com.betopia.hrm.domain.employee.request.InstituteNameRequest;
import com.betopia.hrm.services.employee.InstituteNameService;
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
@RequestMapping("/v1/instituteNames")
@Tag(
        name = "Employee Management -> Create InstituteName setup",
        description = "APIs for configurable FieldStudy. Includes operations to create, read, update, and delete grade."
)
public class InstituteNameController {

    private final InstituteNameService instituteNameService;

    public InstituteNameController(InstituteNameService instituteNameService) {
        this.instituteNameService = instituteNameService;
    }


    @GetMapping
    @Operation(
            summary = "1. Get paginated list of InstituteName",
            description = "Retrieves a paginated list of InstituteName from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch InstituteName records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<InstituteName>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<InstituteName> paginationResponse = instituteNameService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all InstituteName",
            description = "Retrieves a list of all InstituteName available in the system. "
                    + "This endpoint returns the complete InstituteName collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllInstituteName()
    {
        List<InstituteName> instituteNames = instituteNameService.getAllInstituteName();

        GlobalResponse response = GlobalResponse.success(
                instituteNames,
                "All InstituteName fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new InstituteName",
            description = "Creates a new InstituteName in the system with the provided details. "
                    + "Required fields such as name must be included in the request body. "
                    + "Returns the created InstituteName along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody InstituteNameRequest request)
    {
        InstituteName instituteName = instituteNameService.store(request);

        GlobalResponse response = GlobalResponse.success(
                instituteName,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get InstituteName by ID",
            description = "Retrieves the details of a specific InstituteName using the provided ID. "
                    + "If the InstituteName with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long instituteNameId)
    {
        InstituteName instituteName = instituteNameService.show(instituteNameId);

        GlobalResponse response = GlobalResponse.success(
                instituteName,
                "InstituteName fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update InstituteName by ID",
            description = "Updates the details of an existing InstituteName using the provided ID. "
                    + "This endpoint allows modifying InstituteName name or other attributes. "
                    + "If the InstituteName with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long instituteNameId,
                                                 @Valid @RequestBody InstituteNameRequest request)
    {
        InstituteName instituteName = instituteNameService.update(instituteNameId, request);

        GlobalResponse response = GlobalResponse.success(
                instituteName,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete InstituteName by ID",
            description = "Deletes a specific InstituteName from the system using the provided ID. "
                    + "If the InstituteName does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove InstituteName records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long instituteNameId)
    {
        instituteNameService.delete(instituteNameId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "InstituteName deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
