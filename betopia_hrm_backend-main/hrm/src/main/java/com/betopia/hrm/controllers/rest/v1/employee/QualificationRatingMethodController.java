package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.QualificationRatingMethod;
import com.betopia.hrm.domain.employee.request.MethodUpdateRequest;
import com.betopia.hrm.domain.employee.request.QualificationRatingMethodRequest;
import com.betopia.hrm.services.employee.QualificationRatingMethodService;
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
@RequestMapping("/v1/qualificationRatingMethods")
@Tag(
        name = "Employee Management -> QualificationRatingMethod setup",
        description = "APIs for configurable qualificationRatingMethod. Includes operations to create, read, update, and delete grade."
)
public class QualificationRatingMethodController {

    private final QualificationRatingMethodService qualificationRatingMethodService;

    public QualificationRatingMethodController(QualificationRatingMethodService qualificationRatingMethodService) {
        this.qualificationRatingMethodService = qualificationRatingMethodService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of qualificationRatingMethod",
            description = "Retrieves a paginated list of qualificationRatingMethod from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch qualificationRatingMethod records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<QualificationRatingMethod>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<QualificationRatingMethod> paginationResponse = qualificationRatingMethodService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all grades",
            description = "Retrieves a list of all qualificationRatingMethod available in the system. "
                    + "This endpoint returns the complete qualificationRatingMethod collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllQualificationRatingMethod()
    {
        List<QualificationRatingMethod> qualificationRatingMethods = qualificationRatingMethodService.getAllQualificationRatingMethods();

        GlobalResponse response = GlobalResponse.success(
                qualificationRatingMethods,
                "All qualificationRatingMethod fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new qualificationRatingMethod",
            description = "Creates a new grade in the system with the provided details. "
                    + "Required fields such aa code must be included in the request body. "
                    + "Returns the created grade along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody QualificationRatingMethodRequest request)
    {
        QualificationRatingMethod qualificationRatingMethod = qualificationRatingMethodService.insert(request);

        GlobalResponse response = GlobalResponse.success(
                qualificationRatingMethod,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get qualificationRatingMethod by ID",
            description = "Retrieves the details of a specific qualificationRatingMethod using the provided ID. "
                    + "If the qualificationRatingMethod with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long qualificationRatingMethodId)
    {
        QualificationRatingMethod qualificationRatingMethod = qualificationRatingMethodService.show(qualificationRatingMethodId);

        GlobalResponse response = GlobalResponse.success(
                qualificationRatingMethod,
                "QualificationRatingMethod fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update qualificationRatingMethod by ID",
            description = "Updates the details of an existing qualificationRatingMethod using the provided ID. "
                    + "This endpoint allows modifying qualificationRatingMethod code,name or other attributes. "
                    + "If the qualificationRatingMethod with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long qualificationRatingMethodId,
                                                 @Valid @RequestBody MethodUpdateRequest request)
    {
        QualificationRatingMethod qualificationRatingMethod = qualificationRatingMethodService.update(qualificationRatingMethodId, request);

        GlobalResponse response = GlobalResponse.success(
                qualificationRatingMethod,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete qualificationRatingMethod by ID",
            description = "Deletes a specific qualificationRatingMethod from the system using the provided ID. "
                    + "If the qualificationRatingMethod does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove qualificationRatingMethod records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long qualificationRatingMethodId)
    {
        qualificationRatingMethodService.delete(qualificationRatingMethodId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "QualificationRatingMethod deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
