package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.QualificationLevel;
import com.betopia.hrm.domain.employee.request.QualificationLevelRequest;
import com.betopia.hrm.services.employee.QualificationLevelService;
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
@RequestMapping("/v1/qualificationLevels")
@Tag(
        name = "Employee Management -> QualificationLevel setup",
        description = "APIs for configurable QualificationLevel. Includes operations to create, read, update, and delete grade."
)
public class QualificationLevelController {

    private final QualificationLevelService qualificationLevelService;

    public QualificationLevelController(QualificationLevelService qualificationLevelService) {
        this.qualificationLevelService = qualificationLevelService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of QualificationLevel",
            description = "Retrieves a paginated list of QualificationLevel from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch QualificationLevel records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<QualificationLevel>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<QualificationLevel> paginationResponse = qualificationLevelService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all QualificationLevel",
            description = "Retrieves a list of all QualificationLevel available in the system. "
                    + "This endpoint returns the complete QualificationLevel collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllQualificationLevel()
    {
        List<QualificationLevel> qualificationLevels = qualificationLevelService.getAllQualificationLevel();

        GlobalResponse response = GlobalResponse.success(
                qualificationLevels,
                "All qualificationLevels fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new qualificationLevel",
            description = "Creates a new qualificationLevel in the system with the provided details. "
                    + "Required fields such as name must be included in the request body. "
                    + "Returns the created qualificationLevel along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody QualificationLevelRequest request)
    {
        QualificationLevel qualificationLevels = qualificationLevelService.store(request);

        GlobalResponse response = GlobalResponse.success(
                qualificationLevels,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get QualificationLevel by ID",
            description = "Retrieves the details of a specific QualificationLevel using the provided ID. "
                    + "If the QualificationLevel with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long qualificationLevelId)
    {
        QualificationLevel qualificationLevel = qualificationLevelService.show(qualificationLevelId);

        GlobalResponse response = GlobalResponse.success(
                qualificationLevel,
                "QualificationLevel fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update QualificationLevel by ID",
            description = "Updates the details of an existing QualificationLevel using the provided ID. "
                    + "This endpoint allows modifying QualificationLevel name or other attributes. "
                    + "If the QualificationLevel with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long qualificationLevelId,
                                                 @Valid @RequestBody QualificationLevelRequest request)
    {
        QualificationLevel qualificationLevel = qualificationLevelService.update(qualificationLevelId, request);

        GlobalResponse response = GlobalResponse.success(
                qualificationLevel,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete QualificationLevel by ID",
            description = "Deletes a specific QualificationLevel from the system using the provided ID. "
                    + "If the QualificationLevel does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove QualificationLevel records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long qualificationLevelId)
    {
        qualificationLevelService.delete(qualificationLevelId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "QualificationLevel deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
