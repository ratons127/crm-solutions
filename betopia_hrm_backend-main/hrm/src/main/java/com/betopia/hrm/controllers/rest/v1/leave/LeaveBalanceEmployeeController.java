package com.betopia.hrm.controllers.rest.v1.leave;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.leave.entity.LeaveBalanceEmployee;
import com.betopia.hrm.services.leaves.leavebalanceemployee.LeaveBalanceEmployeeService;
import com.betopia.hrm.webapp.util.ResponseBuilder;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 6000)
@RestController
@RequestMapping("/v1/leave-balance-employee")
@Tag(
        name = "Leave Management -> LeaveBalanceEmployee",
        description = "Operations related to leave balance"
)
public class LeaveBalanceEmployeeController {

    private final LeaveBalanceEmployeeService leaveBalanceService;

    public LeaveBalanceEmployeeController(LeaveBalanceEmployeeService leaveBalanceService) {
        this.leaveBalanceService = leaveBalanceService;
    }

    @GetMapping({"/{year}", "/{employeeId}/{year}"})
    @Operation(summary = "1. Get balances for employee in a year", description = "Retrieve employee leave balance in a year")
    public ResponseEntity<GlobalResponse> getBalances(
            @PathVariable(required = false) Long employeeId,
            @PathVariable int year) {
        List<LeaveBalanceEmployee> leaveBalanceEmployees = leaveBalanceService.getBalancesForEmployee(employeeId, year);
        return ResponseBuilder.ok(leaveBalanceEmployees, "All employee leave balance fetched successfully");
    }

    @PostMapping("/initialize/{employeeId}/{year}")
    @Operation(summary = "2. Initialize yearly balances", description = "Initialize yearly balances (e.g., at start of year)")
    public ResponseEntity<GlobalResponse> initializeBalances(
            @PathVariable Long employeeId,
            @PathVariable int year) {
        leaveBalanceService.initializeYearlyBalances(employeeId, year);
        return ResponseBuilder.created(null,"Leave balances initialized for employee " + employeeId + " for year " + year);
    }

    @PostMapping("/year-end/{employeeId}/{year}")
    @Operation(summary = "4. Year-end processing", description = "Year-end processing (carry forward, encashment)")
    public ResponseEntity<GlobalResponse> processYearEnd(
            @PathVariable Long employeeId,
            @PathVariable int year) {
        leaveBalanceService.processYearEnd(employeeId, year);
        return ResponseBuilder.ok(null, "Year-end processing done for employee " + employeeId);
    }

    @PostMapping("/accrue/{employeeId}")
    @Operation(summary = "5. Leave accrual processing", description = "Trigger leave accrual for a single employee and leave type")
    public ResponseEntity<GlobalResponse> accrueLeave(
            @PathVariable Long employeeId,
            @RequestParam Long leaveTypeId,
            @RequestParam(required = false) Integer year,
            @RequestParam Integer daysWorked) {

        int targetYear = (year == null) ? java.time.LocalDate.now().getYear() : year;

        leaveBalanceService.accrueLeave(employeeId, leaveTypeId, targetYear, daysWorked);

        return ResponseBuilder.ok(null, "Accrual applied for employee " + employeeId +
                ", leave type " + leaveTypeId + ", year " + targetYear);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "6. Delete leave balance", description = "Remove a leave balance from the system")
    public ResponseEntity<GlobalResponse> delete(@PathVariable("id") Long id) {
        leaveBalanceService.deleteLeaveBalance(id);
        return ResponseBuilder.noContent("Leave balance deleted successfully");
    }

    @PostMapping("/import")
    public ResponseEntity<?> importFromExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<LeaveBalanceEmployee> imported = leaveBalanceService.importAndSave(file);
            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", imported.size() + " leave balance imported successfully",
                    "count", imported.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                    "success", false,
                    "message", "Import failed: " + e.getMessage()
            ));
        }
    }

}
