package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.FieldStudy;
import com.betopia.hrm.domain.employee.request.FieldStudyRequest;
import com.betopia.hrm.services.employee.FieldStudyService;
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
@RequestMapping("/v1/fieldStudies")
@Tag(
        name = "Employee Management -> Field Study setup",
        description = "APIs for configurable FieldStudy. Includes operations to create, read, update, and delete grade."
)
public class FieldStudyController {

    private final FieldStudyService fieldStudyService;

    public FieldStudyController(FieldStudyService fieldStudyService) {
        this.fieldStudyService = fieldStudyService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of FieldStudy",
            description = "Retrieves a paginated list of FieldStudy from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch FieldStudy records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<FieldStudy>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<FieldStudy> paginationResponse = fieldStudyService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all FieldStudy",
            description = "Retrieves a list of all FieldStudy available in the system. "
                    + "This endpoint returns the complete FieldStudy collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllQualificationLevel()
    {
        List<FieldStudy> fieldStudies = fieldStudyService.getAllFieldStudy();

        GlobalResponse response = GlobalResponse.success(
                fieldStudies,
                "All FieldStudy fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new FieldStudy",
            description = "Creates a new FieldStudy in the system with the provided details. "
                    + "Required fields such as name must be included in the request body. "
                    + "Returns the created FieldStudy along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody FieldStudyRequest request)
    {
        FieldStudy fieldStudy = fieldStudyService.store(request);

        GlobalResponse response = GlobalResponse.success(
                fieldStudy,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get fieldStudy by ID",
            description = "Retrieves the details of a specific fieldStudy using the provided ID. "
                    + "If the fieldStudy with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long fieldStudyId)
    {
        FieldStudy fieldStudy = fieldStudyService.show(fieldStudyId);

        GlobalResponse response = GlobalResponse.success(
                fieldStudy,
                "FieldStudy fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update FieldStudy by ID",
            description = "Updates the details of an existing FieldStudy using the provided ID. "
                    + "This endpoint allows modifying FieldStudy name or other attributes. "
                    + "If the FieldStudy with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long fieldStudyId,
                                                 @Valid @RequestBody FieldStudyRequest request)
    {
        FieldStudy fieldStudy = fieldStudyService.update(fieldStudyId, request);

        GlobalResponse response = GlobalResponse.success(
                fieldStudy,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete FieldStudy by ID",
            description = "Deletes a specific FieldStudy from the system using the provided ID. "
                    + "If the FieldStudy does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove FieldStudy records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long fieldStudyId)
    {
        fieldStudyService.delete(fieldStudyId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "FieldStudy deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
