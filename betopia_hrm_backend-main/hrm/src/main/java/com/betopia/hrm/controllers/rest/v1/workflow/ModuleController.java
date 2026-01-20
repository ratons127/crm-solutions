package com.betopia.hrm.controllers.rest.v1.workflow;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.dto.workflow.ModuleDTO;
import com.betopia.hrm.domain.workflow.request.ModuleRequest;
import com.betopia.hrm.services.workflow.module.ModuleService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
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
@RequestMapping("/v1/modules")
@Tag(
        name = "Workflow -> Modules",
        description = "APIs for managing modules"
)
public class ModuleController {

    private final ModuleService moduleService;

    public ModuleController(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @GetMapping
    @Operation(summary = "1. Get all modules with pagination", description = "Retrieve all modules with pagination")
    public ResponseEntity<PaginationResponse<ModuleDTO>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());
        PaginationResponse<ModuleDTO> paginationResponse = moduleService.index(direction, page, perPage);
        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all modules", description = "Retrieve all modules without pagination")
    public ResponseEntity<GlobalResponse> getAll() {
        List<ModuleDTO> dtos = moduleService.getAll();
        return ResponseBuilder.ok(dtos, "All modules fetched successfully");
    }

    @GetMapping("{id}")
    @Operation(summary = "3. Get module by id", description = "Retrieve a single module by its ID")
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long id) {
        ModuleDTO dto = moduleService.getById(id);
        return ResponseBuilder.ok(dto, "Module fetched successfully");
    }

    @PostMapping("/save")
    @Operation(summary = "4. Save/Store module", description = "Create/Save a new module")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody ModuleRequest request) {
        ModuleDTO stored = moduleService.store(request);
        return ResponseBuilder.created(stored, "Module created successfully");
    }

    @PutMapping("/update/{id}")
    @Operation(summary = "5. Update module", description = "Update an existing module")
    public ResponseEntity<GlobalResponse> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody ModuleRequest request
    ) {
        ModuleDTO dto = moduleService.update(id, request);
        return ResponseBuilder.ok(dto, "Module updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete module", description = "Remove a module from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        moduleService.delete(id);
        return ResponseBuilder.noContent("Module deleted successfully");
    }
}
