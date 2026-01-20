package com.betopia.hrm.controllers.rest.v1.admin;

import com.betopia.hrm.domain.admin.entity.Location;
import com.betopia.hrm.domain.admin.request.LocationRequest;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.pagination.PaginationResponse;
import com.betopia.hrm.services.admin.LocationService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
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
@RequestMapping("/v1/locations")
@Tag(
        name = "Companies Config -> Location setup",
        description = "APIs for configurable location. Includes operations to create, read, update, and delete location."
)
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping
    @Operation(
            summary = "1. Get paginated list of location",
            description = "Retrieves a paginated list of location from the system. "
                    + "Supports parameters such as page number and page size to control the result set. "
                    + "Use this endpoint when you want to fetch location records in a paginated format instead of retrieving all at once."
    )
    public ResponseEntity<PaginationResponse<Location>> index(
            @RequestParam(defaultValue = "DESC") String sortDirection,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int perPage
    ){
        Sort.Direction direction = Sort.Direction.fromString(sortDirection.toUpperCase());

        PaginationResponse<Location> paginationResponse = locationService.index(direction, page, perPage);

        return ResponseEntity.ok(paginationResponse);
    }

    @GetMapping("/all")
    @Operation(
            summary = "2. Get all location",
            description = "Retrieves a list of all location available in the system. "
                    + "This endpoint returns the complete location collection without pagination. "
    )
    public ResponseEntity<GlobalResponse> getAllLocation()
    {
        List<Location> location = locationService.getAllLocations();

        GlobalResponse response = GlobalResponse.success(
                location,
                "All location fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping
    @Operation(
            summary = "3. Create a new location",
            description = "Creates a new location in the system with the provided details. "
                    + "Returns the created location along with its unique ID."
    )
    public ResponseEntity<GlobalResponse> store(@Valid @RequestBody LocationRequest request)
    {
        Location location = locationService.insert(request);

        GlobalResponse response = GlobalResponse.success(
                location,
                "Insert successfully",
                HttpStatus.CREATED.value()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "4. Get location by ID",
            description = "Retrieves the details of a specific location using the provided ID. "
                    + "If the location with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> show(@PathVariable("id") Long locationId)
    {
        Location location = locationService.show(locationId);

        GlobalResponse response = GlobalResponse.success(
                location,
                "Location fetch successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "5. Update location by ID",
            description = "Updates the details of an existing location using the provided ID. "
                    + "This endpoint allows modifying location name,geohash attributes. "
                    + "If the location with the given ID does not exist, a 404 Not Found response will be returned."
    )
    public ResponseEntity<GlobalResponse> update(@PathVariable("id") Long locationId,
                                                 @Valid @RequestBody LocationRequest request)
    {
        Location location = locationService.update(locationId, request);

        GlobalResponse response = GlobalResponse.success(
                location,
                "Update successful",
                HttpStatus.OK.value()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "6. Delete Location by ID",
            description = "Deletes a specific location from the system using the provided ID. "
                    + "If the location does not exist, a 404 Not Found response will be returned. "
                    + "Use this endpoint to permanently remove location records."
    )
    public ResponseEntity<GlobalResponse> destroy(@PathVariable("id") Long locationId)
    {
        locationService.delete(locationId);

        GlobalResponse response = GlobalResponse.success(
                null,
                "Location deleted successfully",
                HttpStatus.NO_CONTENT.value()
        );

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }
    @GetMapping("/divisions/{countryId}")
    @Operation(
            summary = "6. Retrieve All Division",
            description = "Retrieve a specific division from the location record. "
                    + "Use this endpoint to all division from location records."
    )
    public ResponseEntity<GlobalResponse> retrieveAllDivision(@PathVariable("countryId") Integer countryId)
    {
        List<Location> divisions = locationService.getAllDivisions(countryId);
        if(!divisions.isEmpty())
            return ResponseBuilder.ok(divisions,"Division List fetch successful");
        else
           return ResponseBuilder.ok(null,"No data found for division");

    }

    @GetMapping("districts/{divisionId}")
    @Operation(
            summary = "6. Retrieve All District by division id",
            description = "Use this endpoint to retrieve all district by division id from location records. "

    )
    public ResponseEntity<GlobalResponse> retrieveDistrictByDistrict(@PathVariable("divisionId") Integer divisionId)
    {
        List<Location> districts = locationService.getDistrictsByDivisionId(divisionId);
        if(!districts.isEmpty())
            return ResponseBuilder.ok(districts,"District List fetch successful");
        else
            return ResponseBuilder.ok(null,"No data found for district");
    }

    @GetMapping("/policeStations/{districtId}")
    @Operation(
            summary = "6. Retrieve All Police station by district id",
            description = "Use this endpoint to retrieve all police station by district id from location records. "

    )
    public ResponseEntity<GlobalResponse> retrievePoliceStationDistrictBy(@PathVariable("districtId") Integer districtId)
    {
        List<Location> policeStations = locationService.getPoliceStationsByDistrictId(districtId);
        if(!policeStations.isEmpty())
            return ResponseBuilder.ok(policeStations,"Police Station List fetch successful");
        else
            return ResponseBuilder.ok(null,"No data found for police station");
    }

    @GetMapping("/postOffices/{districtId}")
    @Operation(
            summary = "6. Retrieve All Post Office by district id",
            description = "Use this endpoint to retrieve all Post Office by district id from location records. "

    )
    public ResponseEntity<GlobalResponse> retrievePostOfficeDistrictBy(@PathVariable("districtId") Integer districtId)
    {
        List<Location> postOffices = locationService.getPoliceStationsByDistrictId(districtId);
        if(!postOffices.isEmpty())
            return ResponseBuilder.ok(postOffices,"Post Office List fetch successful");
        else
            return ResponseBuilder.ok(null,"No data found for post office");
    }
}
