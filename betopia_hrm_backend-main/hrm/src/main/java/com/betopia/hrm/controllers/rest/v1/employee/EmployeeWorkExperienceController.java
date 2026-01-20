package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.EmployeeWorkExperience;
import com.betopia.hrm.domain.employee.request.EmployeeWorkExperienceRequest;
import com.betopia.hrm.services.employee.EmployeeWorkExperienceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/employeeWorkExperiences")
@Tag(
        name = "Employee Management -> EmployeeWorkExperience setup",
        description = "APIs for configurable EmployeeWorkExperience. Includes operations to create, read, update, and delete employee types."
)
public class EmployeeWorkExperienceController {

    private final EmployeeWorkExperienceService employeeWorkExperienceService;

    public EmployeeWorkExperienceController(EmployeeWorkExperienceService employeeWorkExperienceService) {
        this.employeeWorkExperienceService = employeeWorkExperienceService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of EmployeeWorkExperience",
            description = "Retrieves a paginated list of EmployeeWorkExperience from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch EmployeeWorkExperience records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<EmployeeWorkExperience>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<EmployeeWorkExperience> paginationResponse = employeeWorkExperienceService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all EmployeeWorkExperience ",
            description = "Retrieves a list of all EmployeeWorkExperience available in the system. "
                    + "This endpoint returns the complete EmployeeWorkExperience collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllEmployeeWorkExperience()
    {
        List<EmployeeWorkExperience> employeeWorkExperiences = employeeWorkExperienceService.getAllEmployeeWorkExperience();

        GlobalResponse response = GlobalResponse.success(
                employeeWorkExperiences,
                "All employeeWorkExperiences fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new EmployeeWorkExperience",
            description = "Creates a new EmployeeWorkExperience in the system with the provided details. "
                    + "Required fields such as minLength,maxLength must be included in the request body. "
                    + "Returns the created EmployeeWorkExperience along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody EmployeeWorkExperienceRequest request)
    {
        EmployeeWorkExperience employeeWorkExperiences = employeeWorkExperienceService.store(request);

        GlobalResponse response = GlobalResponse.success(
                employeeWorkExperiences,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get EmployeeWorkExperience by ID",
            description = "Retrieves the details of a specific EmployeeWorkExperience using the provided ID. "
                    + "If the EmployeeWorkExperience with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long employeeWorkExperienceId)
    {
        EmployeeWorkExperience employeeWorkExperience = employeeWorkExperienceService.show(employeeWorkExperienceId);

        GlobalResponse response = GlobalResponse.success(
                employeeWorkExperience,
                "EmployeeWorkExperience fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update EmployeeWorkExperience by ID",
            description = "Updates the details of an existing EmployeeWorkExperience using the provided ID. "
                    + "This endpoint allows modifying EmployeeWorkExperience company name,job description or other attributes. "
                    + "If the EmployeeWorkExperience with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long employeeWorkExperienceId,
                                                 @Valid @RequestBody EmployeeWorkExperienceRequest request)
    {
        EmployeeWorkExperience employeeWorkExperience = employeeWorkExperienceService.update(employeeWorkExperienceId, request);

        GlobalResponse response = GlobalResponse.success(
                employeeWorkExperience,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete EmployeeWorkExperience by ID",
            description = "Deletes a specific employee types from the system using the provided ID. "
                    + "If the EmployeeWorkExperience does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove EmployeeWorkExperience records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long employeeWorkExperienceId)
    {
        employeeWorkExperienceService.delete(employeeWorkExperienceId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "EmployeeWorkExperience deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @PostMapping(value = "/{id}/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload EmployeeWorkExperience file", description = "Upload File to S3 and store reference in DB")
    public ResponseEntity<GlobalResponse> uploadFile(
            @PathVariable("id") Long employeeWorkExperienceId,
            @RequestParam("file") MultipartFile file
    ) {
        return employeeWorkExperienceService.uploadFile(employeeWorkExperienceId, file);
    }

    @DeleteMapping("{id}/delete-file")
    @Operation(summary = "Delete EmployeeWorkExperience file", description = "Delete File from S3 and remove reference in DB")
    public ResponseEntity<GlobalResponse> deleteFile(@PathVariable("id") Long employeeWorkExperienceId) {
        return employeeWorkExperienceService.deleteFile(employeeWorkExperienceId);
    }
}
