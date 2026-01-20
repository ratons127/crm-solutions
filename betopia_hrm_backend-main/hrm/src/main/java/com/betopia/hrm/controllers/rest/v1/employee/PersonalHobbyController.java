package com.betopia.hrm.controllers.rest.v1.employee;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.employee.entity.PersonalHobby;
import com.betopia.hrm.domain.employee.request.PersonalHobbyRequest;
import com.betopia.hrm.services.employee.PersonalHobbyService;
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
@RequestMapping("/v1/personalHobbies")
@Tag(
        name = "Employee Management -> Create PersonalHobby setup",
        description = "APIs for configurable PersonalHobby. Includes operations to create, read, update, and delete grade."
)
public class PersonalHobbyController {

    private final PersonalHobbyService personalHobbyService;

    public PersonalHobbyController(PersonalHobbyService personalHobbyService) {
        this.personalHobbyService = personalHobbyService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of PersonalHobby",
            description = "Retrieves a paginated list of PersonalHobby from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch PersonalHobby records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<PersonalHobby>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<PersonalHobby> paginationResponse = personalHobbyService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all PersonalHobby",
            description = "Retrieves a list of all PersonalHobby available in the system. "
                    + "This endpoint returns the complete PersonalHobby collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllPersonalHobby()
    {
        List<PersonalHobby> personalHobbies = personalHobbyService.getAllPersonalHobby();

        GlobalResponse response = GlobalResponse.success(
                personalHobbies,
                "All PersonalHobby fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new PersonalHobby",
            description = "Creates a new PersonalHobby in the system with the provided details. "
                    + "Required fields such as name must be included in the request body. "
                    + "Returns the created PersonalHobby along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody PersonalHobbyRequest request)
    {
        PersonalHobby personalHobby = personalHobbyService.store(request);

        GlobalResponse response = GlobalResponse.success(
                personalHobby,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get PersonalHobby by ID",
            description = "Retrieves the details of a specific PersonalHobby using the provided ID. "
                    + "If the PersonalHobby with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long personalHobbyId)
    {
        PersonalHobby personalHobby = personalHobbyService.show(personalHobbyId);

        GlobalResponse response = GlobalResponse.success(
                personalHobby,
                "PersonalHobby fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update PersonalHobby by ID",
            description = "Updates the details of an existing PersonalHobby using the provided ID. "
                    + "This endpoint allows modifying InstituteName name or other attributes. "
                    + "If the PersonalHobby with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long personalHobbyId,
                                                 @Valid @RequestBody PersonalHobbyRequest request)
    {
        PersonalHobby personalHobby = personalHobbyService.update(personalHobbyId, request);

        GlobalResponse response = GlobalResponse.success(
                personalHobby,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete PersonalHobby by ID",
            description = "Deletes a specific PersonalHobby from the system using the provided ID. "
                    + "If the PersonalHobby does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove PersonalHobby records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long personalHobbyId)
    {
        personalHobbyService.delete(personalHobbyId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Personal Hobby deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
}
