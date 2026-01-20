package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.Grade;
import com.betopia.hrm.domain.employee.request.GradeRequest;
import com.betopia.hrm.services.employee.GradeService;
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
@RequestMapping("/v1/grades")
@Tag(
        name = "Employee Management -> Grade setup",
        description = "APIs for configurable grade. Includes operations to create, read, update, and delete grade."
)
public class GradeController {

    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of grade",
            description = "Retrieves a paginated list of grade from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch grade records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<Grade>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Grade> paginationResponse = gradeService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all grades",
            description = "Retrieves a list of all grades available in the system. "
                    + "This endpoint returns the complete grade collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllEmployeeTypes()
    {
        List<Grade> grades = gradeService.getAllGrades();

        GlobalResponse response = GlobalResponse.success(
                grades,
                "All grades fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new grade",
            description = "Creates a new grade in the system with the provided details. "
                    + "Required fields such aa code must be included in the request body. "
                    + "Returns the created grade along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody GradeRequest request)
    {
        Grade grade = gradeService.insert(request);

        GlobalResponse response = GlobalResponse.success(
                grade,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get grade by ID",
            description = "Retrieves the details of a specific grade using the provided ID. "
                    + "If the grade with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long gradeId)
    {
        Grade grade = gradeService.show(gradeId);

        GlobalResponse response = GlobalResponse.success(
                grade,
                "Grade fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update grade by ID",
            description = "Updates the details of an existing grade using the provided ID. "
                    + "This endpoint allows modifying grade code,name,description or other attributes. "
                    + "If the grade with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long gradeId,
                                                 @Valid @RequestBody GradeRequest request)
    {
        Grade grade = gradeService.update(gradeId, request);

        GlobalResponse response = GlobalResponse.success(
                grade,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete grade by ID",
            description = "Deletes a specific grade from the system using the provided ID. "
                    + "If the grade does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove grade records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long gradeId)
    {
        gradeService.delete(gradeId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Grade deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
