package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.QualificationType;
import com.betopia.hrm.domain.employee.request.QualificationTypeRequest;
import com.betopia.hrm.services.employee.QualificationTypeService;
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
@RequestMapping("/v1/qualificationTypes")
@Tag(
        name = "Employee Management -> QualificationType setup",
        description = "APIs for configurable QualificationType. Includes operations to create, read, update, and delete grade."
)
public class QualificationTypeController {

    private final QualificationTypeService qualificationTypeService;

    public QualificationTypeController(QualificationTypeService qualificationTypeService) {
        this.qualificationTypeService = qualificationTypeService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of qualificationType",
            description = "Retrieves a paginated list of qualificationType from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch qualificationType records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<QualificationType>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<QualificationType> paginationResponse = qualificationTypeService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all QualificationType",
            description = "Retrieves a list of all QualificationType available in the system. "
                    + "This endpoint returns the complete QualificationType collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllQualificationTypes()
    {
        List<QualificationType> qualificationTypes = qualificationTypeService.getAllQualificationType();

        GlobalResponse response = GlobalResponse.success(
                qualificationTypes,
                "All qualificationTypes fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new qualificationTypes",
            description = "Creates a new qualificationTypes in the system with the provided details. "
                    + "Required fields such as name must be included in the request body. "
                    + "Returns the created qualificationTypes along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody QualificationTypeRequest request)
    {
        QualificationType qualificationType = qualificationTypeService.store(request);

        GlobalResponse response = GlobalResponse.success(
                qualificationType,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get qualificationType by ID",
            description = "Retrieves the details of a specific qualificationType using the provided ID. "
                    + "If the qualificationType with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long qualificationTypeId)
    {
        QualificationType qualificationType = qualificationTypeService.show(qualificationTypeId);

        GlobalResponse response = GlobalResponse.success(
                qualificationType,
                "QualificationType fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update QualificationType by ID",
            description = "Updates the details of an existing QualificationType using the provided ID. "
                    + "This endpoint allows modifying QualificationType name,description or other attributes. "
                    + "If the QualificationType with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long gradeId,
                                                 @Valid @RequestBody QualificationTypeRequest request)
    {
        QualificationType qualificationType = qualificationTypeService.update(gradeId, request);

        GlobalResponse response = GlobalResponse.success(
                qualificationType,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete QualificationType by ID",
            description = "Deletes a specific QualificationType from the system using the provided ID. "
                    + "If the QualificationType does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove grade records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long qualificationTypeId)
    {
        qualificationTypeService.delete(qualificationTypeId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "QualificationType deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
