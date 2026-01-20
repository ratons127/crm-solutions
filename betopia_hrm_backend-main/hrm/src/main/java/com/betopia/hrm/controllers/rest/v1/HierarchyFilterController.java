package com.betopia.hrm.controllers.rest.v1;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.dto.hierarchyfilter.HierarchyFilterDTO;
import com.betopia.hrm.services.hierarchyfilterservice.HierarchyFilterService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/hierarchy-filter")
@Tag( name = "Hierarchy Filter API", description = "This API provides hierarchical filtering data"
)
public class HierarchyFilterController {

    private final HierarchyFilterService hierarchyFilterService;

    public HierarchyFilterController(HierarchyFilterService hierarchyFilterService) {
        this.hierarchyFilterService = hierarchyFilterService;
    }

    @GetMapping
    @Operation(summary = "1.Get hierarchy filter data" , description = "Retrieve all hierarchy filter data without pagination")
    public ResponseEntity<GlobalResponse> getHierarchy(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long businessUnitId,
            @RequestParam(required = false) Long workplaceGroupId,
            @RequestParam(required = false) Long workplaceId,
            @RequestParam(required = false) Long departmentId
    ) {
        HierarchyFilterDTO response = hierarchyFilterService.getHierarchyFilterData(
                companyId,
                businessUnitId,
                workplaceGroupId,
                workplaceId,
                departmentId
        );
        return ResponseBuilder.ok(response, "Hierarchy filter data fetch successfully");
    }

}