package com.betopia.hrm.controllers.rest.v1.company;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.domain.company.entity.Team;
import com.betopia.hrm.domain.company.request.TeamRequest;
import com.betopia.hrm.services.company.team.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/teams")
@Tag(name = "Company -> team", description = "Operations related to managing team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    @Operation(summary = "1. Get all team with pagination", description = "Retrieve a list of all team with pagination.")
    //@PreAuthorize("hasAuthority('team-list')")
    public ResponseEntity<PaginationResponse<Team>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Team> paginationResponse = teamService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(summary = "2. Get all team without pagination", description = "Retrieve a list of all team without pagination")
    //@PreAuthorize("hasAuthority('team-list')")
    public ResponseEntity<GlobalResponse> getAllTeams()
    {
        List<Team> teams = teamService.getAllTeams();

        GlobalResponse response = GlobalResponse.success(
                teams,
                "All teams fetch successful",
                200
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(summary = "3. Store team", description = "Creating a new team")
    //@PreAuthorize("hasAuthority('team-create')")
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody TeamRequest request)
    {
        Team team = teamService.store(request);

        GlobalResponse response = GlobalResponse.success(
                team,
                "Team created successfully",
                201
        );

        return ResponseEntity.status(201).body(response);
    }

    @GetMapping("{id}")
    @Operation(summary = "4. Team get by id", description = "Return a single team by id")
    //@PreAuthorize("hasAuthority('team-edit')")
    public ResponseEntity<GlobalResponse> edit(@PathVariable("id") Long teamId)
    {
        Team team = teamService.edit(teamId);

        GlobalResponse response = GlobalResponse.success(
                team,
                "Team fetch successful",
                200
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(summary = "5. Update team", description = "Update an existing team by id")
    //@PreAuthorize("hasAuthority('team-edit')")
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long teamId, @Valid @RequestBody TeamRequest request)
    {
        Team team = teamService.update(teamId, request);

        GlobalResponse response = GlobalResponse.success(
                team,
                "Team updated successfully",
                200
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "6. Delete team", description = "Delete an existing team by id")
    //@PreAuthorize("hasAuthority('team-delete')")
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long teamId)
    {
        teamService.destroy(teamId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Team deleted successfully",
                200
        );

        return ResponseEntity.ok(response);
    }
}
