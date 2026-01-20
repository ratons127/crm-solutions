package com.betopia.hrm.services.leaves.leavebalanceemployee.impl;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.employee.entity.EmployeeType;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import com.betopia.hrm.domain.leave.entity.LeaveBalanceEmployee;
import com.betopia.hrm.domain.leave.entity.LeaveGroupAssign;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.domain.leave.entity.LeaveType;
import com.betopia.hrm.domain.leave.exception.leavebalanceemployee.LeaveBalanceEmployeeNotFoundException;
import com.betopia.hrm.domain.leave.exception.leavepolicy.ValidationException;
import com.betopia.hrm.domain.leave.exception.leavetype.LeaveTypeNotFoundException;
import com.betopia.hrm.domain.leave.repository.LeaveBalanceEmployeeRepository;
import com.betopia.hrm.domain.leave.repository.LeavePolicyRepository;
import com.betopia.hrm.domain.leave.repository.LeaveRequestRepository;
import com.betopia.hrm.domain.leave.repository.LeaveTypeRepository;
import com.betopia.hrm.services.leaves.leaveGroupAssign.LeaveGroupAssignService;
import com.betopia.hrm.services.leaves.leavebalanceemployee.LeaveBalanceEmployeeService;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.LeaveRuleEngine;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.ValidationResult;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class LeaveBalanceEmployeeImpl implements LeaveBalanceEmployeeService {

    private final LeaveBalanceEmployeeRepository balanceRepository;
    private final LeavePolicyRepository policyRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveRuleEngine leaveRuleEngine;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveGroupAssignService leaveGroupAssignService;

    public LeaveBalanceEmployeeImpl(
            LeaveBalanceEmployeeRepository balanceRepository,
            LeavePolicyRepository policyRepository,
            LeaveTypeRepository leaveTypeRepository,
            EmployeeRepository employeeRepository,
            LeaveRuleEngine leaveRuleEngine,
            LeaveRequestRepository leaveRequestRepository,
            LeaveGroupAssignService leaveGroupAssignService
    ) {
        this.balanceRepository = balanceRepository;
        this.policyRepository = policyRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.employeeRepository = employeeRepository;
        this.leaveRuleEngine = leaveRuleEngine;
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveGroupAssignService = leaveGroupAssignService;
    }

    // Fetch balances for employee
    @Override
    public List<LeaveBalanceEmployee> getBalancesForEmployee(Long employeeId, int year) {
        if (employeeId == null) {
            return balanceRepository.findByYear(year);
        }
        return balanceRepository.findByEmployeeIdAndYear(employeeId, year);
    }

    // Initialize leave balances for new year
    @Transactional
    @Override
    public void initializeYearlyBalances(Long employeeId, int year) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EmployeeNotFound("Employee not found with id: " + employeeId));

        List<LeaveGroupAssign> leaveGroupAssigns = leaveGroupAssignService.findApplicableLeaveGroupAssign(employee);

        List<Long> assignIds = leaveGroupAssigns.stream()
                .map(LeaveGroupAssign::getId)
                .toList();

        List<LeavePolicy> allPolicies = policyRepository.findByLeaveGroupAssignIds(assignIds);

        long tenureDays = ChronoUnit.DAYS.between(employee.getDateOfJoining(), LocalDate.now());
        Long employeeTypeId = Optional.ofNullable(employee.getEmployeeType())
                .map(EmployeeType::getId)
                .orElse(null);

        List<LeavePolicy> applicablePolicies = allPolicies.stream()
                .filter(p -> (p.getEmployeeTypeId() != null && p.getEmployeeTypeId().equals(employeeTypeId))
                        || (p.getEmployeeTypeId() == null && p.getTenureRequiredDays() != null && tenureDays >= p.getTenureRequiredDays()))
                .toList();
        if (applicablePolicies.isEmpty()) {
            throw new IllegalStateException("No leave policies configured. Cannot initialize yearly balances.");
        }

        for (LeavePolicy policy : applicablePolicies) {
            // Skip CTO (since it’s earned dynamically)
            if (Boolean.TRUE.equals(policy.getLinkedToOvertime())) continue;

            LeaveBalanceEmployee balance = getLeaveBalanceEmployee(employeeId, year, policy);
            if (balance != null) {
                balanceRepository.save(balance);
            }
        }
    }

    private LeaveBalanceEmployee getLeaveBalanceEmployee(Long employeeId, int year, LeavePolicy policy) {
        LeaveBalanceEmployee balance = null;

        Optional<LeaveBalanceEmployee> balanceExists = balanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, policy.getLeaveType().getId(), year);
        if (balanceExists.isEmpty()) {
            balance = new LeaveBalanceEmployee();
            balance.setEmployeeId(employeeId); // assuming you simplified Employee ref → employeeId
            balance.setLeaveType(policy.getLeaveType());
            balance.setYear(year);
            balance.setEntitledDays(BigDecimal.valueOf(policy.getDefaultQuota()));
            balance.setCarriedForward(BigDecimal.ZERO);
            balance.setEncashed(BigDecimal.ZERO);
            balance.setUsedDays(BigDecimal.ZERO);
            balance.recalculateBalance();
        }
        return balance;
    }

    // Deduct leave from balance
    @Transactional
    @Override
    public boolean applyLeave(LeaveRequest request) {
        Employee employee = getEmployee(request.getEmployeeId());
        LeaveType leaveType = getLeaveType(request.getLeaveType().getId());
        LeaveGroupAssign leaveGroupAssign = validateLeaveGroupAssignee(employee, leaveType);
        LeavePolicy policy = getLeavePolicy(employee, leaveType, leaveGroupAssign);
        request.setLeaveGroupAssign(leaveGroupAssign);

        validateLeaveBalance(getLeaveBalance(request, leaveType), request.getDaysRequested());
        validateLeaveRequest(employee, request, policy);
        validateAndAssignCoveringEmployee(request);

        return true;
    }

    private LeaveGroupAssign validateLeaveGroupAssignee(Employee employee, LeaveType leaveType) {
        return leaveGroupAssignService.findApplicableLeaveGroupAssignByEmployee(employee, leaveType);
    }

    // Apply carry forward at year-end
    @Transactional
    @Override
    public void processYearEnd(Long employeeId, int year) {
        carryForwardBalances(employeeId, year);
    }

    @Override
    @Transactional
    public void accrueLeave(Long employeeId, Long leaveTypeId, int year, Integer daysWorked) {

        LeaveType leaveType = leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new RuntimeException("LeaveType not found: " + leaveTypeId));

        LeavePolicy policy = policyRepository.findByLeaveType(leaveType)
                .orElseThrow(() -> new RuntimeException("Policy not found for leave type: " + leaveType.getName()));

        LeaveBalanceEmployee balance = balanceRepository
                .findByEmployeeIdAndLeaveTypeAndYear(employeeId, leaveType, year)
                .orElseThrow(() -> new RuntimeException("Balance not initialized"));

        // Accrual-based leave
        if (isAccrualBased(policy)) {
            BigDecimal entitled = calculateAccrualEntitled(balance, policy);
            updateBalance(balance, entitled);
        }

        // Earned leave
        if (Boolean.TRUE.equals(policy.getEarnedLeave()) && daysWorked != null) {
            BigDecimal earned = calculateEarnedLeave(policy, BigDecimal.valueOf(daysWorked));
            updateBalance(balance, earned);
        }

        balanceRepository.save(balance);
    }


    private boolean isAccrualBased(LeavePolicy policy) {
        return policy.getAccrualRatePerMonth() != null && policy.getAccrualRatePerMonth() > 0;
    }

    private BigDecimal calculateAccrualEntitled(LeaveBalanceEmployee balance, LeavePolicy policy) {
        BigDecimal entitled = balance.getEntitledDays() == null ? BigDecimal.ZERO : balance.getEntitledDays();
        entitled = entitled.add(BigDecimal.valueOf(policy.getAccrualRatePerMonth()));

        if (policy.getDefaultQuota() != null && entitled.compareTo(BigDecimal.valueOf(policy.getDefaultQuota())) > 0) {
            entitled = BigDecimal.valueOf(policy.getDefaultQuota());
        }
        return entitled;
    }

    private BigDecimal calculateEarnedLeave(LeavePolicy policy, BigDecimal daysWorked) {
        if (policy.getEarnedAfterDays() == null || policy.getEarnedLeaveDays() == null) {
            return BigDecimal.ZERO; // guard clause
        }

        BigDecimal earnedAfterDays = BigDecimal.valueOf(policy.getEarnedAfterDays());
        BigDecimal earnedLeaveDays = BigDecimal.valueOf(policy.getEarnedLeaveDays());

        if (earnedAfterDays.compareTo(BigDecimal.ZERO) <= 0) {
            return BigDecimal.ZERO; // avoid division by zero or negative values
        }

        // (daysWorked / earnedAfterDays) * earnedLeaveDays
        return daysWorked
                .divide(earnedAfterDays, 2, RoundingMode.HALF_UP) // scale = 2 for precision
                .multiply(earnedLeaveDays);
    }

    private void updateBalance(LeaveBalanceEmployee balance, BigDecimal entitledIncrement) {
        BigDecimal entitled = (balance.getEntitledDays() == null ? BigDecimal.ZERO : balance.getEntitledDays()).add(entitledIncrement);
        BigDecimal currentBalance = (balance.getBalance() == null ? BigDecimal.ZERO : balance.getBalance()).add(entitledIncrement);

        balance.setEntitledDays(entitled);
        balance.setBalance(currentBalance);
        balance.recalculateBalance();
    }

    public void carryForwardBalances(Long employeeId, int year) {
        List<LeaveBalanceEmployee> balances = balanceRepository.findByEmployeeIdAndYear(employeeId, year);

        for (LeaveBalanceEmployee balance : balances) {
            LeavePolicy policy = policyRepository.findByLeaveType(balance.getLeaveType())
                    .orElseThrow(() -> new RuntimeException("Policy not found"));

            if (Boolean.TRUE.equals(policy.getCarryForwardAllowed())) {
                BigDecimal remaining = balance.getBalance();
                BigDecimal carryForwardCap = policy.getCarryForwardCap() != null ? BigDecimal.valueOf(policy.getCarryForwardCap()) : remaining;
                BigDecimal daysToCarry = remaining.min(carryForwardCap);

                balance.setBalance(daysToCarry);
            } else {
                balance.setBalance(BigDecimal.ZERO); // reset to 0 if not allowed
            }

            balanceRepository.save(balance);
        }
    }

    private Employee getEmployee(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new LeaveBalanceEmployeeNotFoundException("Employee not found: " + employeeId));
    }

    private LeaveType getLeaveType(Long leaveTypeId) {
        return leaveTypeRepository.findById(leaveTypeId)
                .orElseThrow(() -> new LeaveTypeNotFoundException("LeaveType not found: " + leaveTypeId));
    }

    private LeavePolicy getLeavePolicy(Employee employee, LeaveType leaveType, LeaveGroupAssign leaveGroupAssign) {
        long tenureDays = ChronoUnit.DAYS.between(employee.getDateOfJoining(), LocalDate.now());
        Long employeeTypeId = Optional.ofNullable(employee.getEmployeeType())
                .map(EmployeeType::getId)
                .orElse(null);
        List<LeavePolicy> policies = policyRepository.findByLeaveTypeAndLeaveGroupAssign(leaveType, leaveGroupAssign);

        Optional<LeavePolicy> matchedPolicy = policies.stream()
                .filter(p -> (p.getEmployeeTypeId() != null && p.getEmployeeTypeId().equals(employeeTypeId))
                        || (p.getEmployeeTypeId() == null && p.getTenureRequiredDays() != null && tenureDays >= p.getTenureRequiredDays()))
                .findFirst();

        return matchedPolicy.orElseThrow(() ->
                new IllegalStateException("No applicable leave policy configured. Cannot process leave request.")
        );
    }

    private LeaveBalanceEmployee getLeaveBalance(LeaveRequest request, LeaveType leaveType) {
        return balanceRepository.findByEmployeeIdAndLeaveTypeAndYear(
                        request.getEmployeeId(),
                        leaveType,
                        request.getStartDate().getYear()
                )
                .orElseThrow(() -> new LeaveBalanceEmployeeNotFoundException("Leave balance not found"));
    }

    private void validateLeaveRequest(Employee employee, LeaveRequest request, LeavePolicy policy) {
        ValidationResult result = leaveRuleEngine.validateRequest(employee, request, policy);
        if (!result.isValid()) {
            throw new ValidationException(result.getErrorCode(), result.getMessage());
        }
    }

    private void validateLeaveBalance(LeaveBalanceEmployee balance, BigDecimal daysRequested) {
        if (!balance.hasSufficientBalance(daysRequested)) {
            throw new ValidationException("INSUFFICIENT_BALANCE", "Not enough leave balance");
        }
    }

    private void deductLeave(LeaveBalanceEmployee balance, BigDecimal daysRequested) {
        boolean success = balance.consumeLeave(daysRequested);
        if (!success) {
            throw new ValidationException("INSUFFICIENT_BALANCE", "Not enough leave balance");
        }
        balanceRepository.save(balance);
    }

    private void validateAndAssignCoveringEmployee(LeaveRequest request) {
        if (request.getCoveringEmployeeId() != null) {
            Employee covering = employeeRepository.findById(request.getCoveringEmployeeId())
                    .orElseThrow(() -> new ValidationException("COVERING_EMPLOYEE_NOT_FOUND", "Invalid covering employee"));

            boolean coveringUnavailable = leaveRequestRepository.existsByEmployeeIdAndStartDateBetween(
                    covering.getId(),
                    request.getStartDate(),
                    request.getEndDate()
            );

            if (coveringUnavailable) {
                throw new ValidationException("COVERING_EMPLOYEE_UNAVAILABLE",
                        "Covering employee is already on leave during this period");
            }

            request.setCoveringEmployeeId(covering.getId());
        }
    }

    @Override
    public void deductLeaveBalance(LeaveRequest leaveRequest) {
        Long employeeId = leaveRequest.getEmployeeId();
        Long leaveTypeId = leaveRequest.getLeaveType().getId();
        int year = leaveRequest.getStartDate().getYear();

        LeaveBalanceEmployee balance = balanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveTypeId, year)
                .orElseThrow(() -> new RuntimeException("Leave balance not found for employee: " + employeeId));

        BigDecimal daysToConsume = leaveRequest.getDaysRequested();
        boolean deducted = balance.consumeLeave(daysToConsume);

        if (!deducted) {
            throw new RuntimeException("Insufficient leave balance for employee: " + employeeId);
        }

        balance.recalculateBalance();
        balanceRepository.save(balance);

        System.out.println("✅ Deducted " + daysToConsume + " days for employee " + employeeId +
                ". New balance: " + balance.getBalance());
    }

    @Override
    public void deleteLeaveBalance(Long id) {
        LeaveBalanceEmployee balance = balanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave balance not found with id: " + id));

        balanceRepository.delete(balance);
    }

    /**
     * Import with Parent-Child relationship support
     */

    @Override
    public List<LeaveBalanceEmployee> importAndSave(MultipartFile file) throws Exception {
        List<LeaveBalanceEmployee> balances = new ArrayList<>();

        System.out.println("=== LEAVE BALANCE IMPORT STARTED ===");

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (!rows.hasNext()) {
                System.out.println("Excel file is empty!");
                return balances;
            }

            Row headerRow = rows.next();
            Map<String, Integer> headerMap = new HashMap<>();
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                Cell cell = headerRow.getCell(i);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    String header = cell.getStringCellValue().trim().toLowerCase();
                    headerMap.put(header, i);
                }
            }
            System.out.println("Header Map: " + headerMap);

            int rowNum = 1;
            int inserted = 0;
            int updated = 0;
            int skipped = 0;

            while (rows.hasNext()) {
                Row row = rows.next();
                rowNum++;

                LeaveBalanceEmployee balance = new LeaveBalanceEmployee();

                // --- Employee Serial ID ---
                Integer empIdx = headerMap.get("employeeserialid");
                if (empIdx == null) {
                    System.out.println("Row " + rowNum + ": Missing 'employeeserialid' column!");
                    continue;
                }

                Long employeeSerialId = getLongValue(row, empIdx);
                if (employeeSerialId == null) {
                    System.out.println("Row " + rowNum + ": Invalid or empty employeeserialid");
                    continue;
                }

                Optional<Employee> employeeOpt = employeeRepository.findByEmployeeSerialId(employeeSerialId.intValue());
                if (employeeOpt.isEmpty()) {
                    System.out.println("Row " + rowNum + ": No employee found for serialId " + employeeSerialId);
                    continue;
                }

                Employee employee = employeeOpt.get();
                balance.setEmployeeId(employee.getId());

                // --- Leave Type ---
                Integer leaveTypeIdx = headerMap.get("leavetype");
                if (leaveTypeIdx == null) {
                    System.out.println("Row " + rowNum + ": Missing 'leavetype' column!");
                    continue;
                }

                String leaveTypeName = getStringValue(row, leaveTypeIdx);
                if (leaveTypeName == null || leaveTypeName.isEmpty()) {
                    System.out.println("Row " + rowNum + ": Empty leavetype cell");
                    continue;
                }

                LeaveType leaveType = leaveTypeRepository.findByNameIgnoreCase(leaveTypeName)
                        .orElse(null);

                if (leaveType == null) {
                    System.out.println("Row " + rowNum + ": LeaveType not found: " + leaveTypeName);
                    continue;
                }

                // Maternity leave restriction
                if (leaveTypeName.equalsIgnoreCase("Maternity Leave")) {
                    String gender = employee.getGender() != null ? employee.getGender().trim().toLowerCase() : "";
                    if (!gender.equals("female")) {
                        System.out.println("Row " + rowNum + ": Skipped - Maternity Leave not allowed for male employee (EmpSerial: "
                                + employee.getEmployeeSerialId() + ")");
                        skipped++;
                        continue;
                    }
                }

                balance.setLeaveType(leaveType);

                // --- Year ---
                Integer yearIdx = headerMap.get("year");
                Integer year = getIntegerValue(row, yearIdx);
                if (year == null) {
                    year = LocalDate.now().getYear();
                }
                balance.setYear(year);

                // --- Read numeric fields ---
                BigDecimal entitledDays = readBigDecimalCell(row, headerMap.get("entitleddays"));
                BigDecimal carryForward = readBigDecimalCell(row, headerMap.get("carryforward"));
                BigDecimal encashed = readBigDecimalCell(row, headerMap.get("encashed"));
                BigDecimal usedDays = readBigDecimalCell(row, headerMap.get("useddays"));
                BigDecimal balanceValue = readBigDecimalCell(row, headerMap.get("balance"));

                balance.setEntitledDays(entitledDays);
                balance.setCarriedForward(carryForward);
                balance.setEncashed(encashed);
                balance.setUsedDays(usedDays);
                balance.setBalance(balanceValue);

                // --- Skip all zero rows ---
                boolean allZero = (entitledDays.compareTo(BigDecimal.ZERO) == 0)
                        && (usedDays.compareTo(BigDecimal.ZERO) == 0)
                        && (balanceValue.compareTo(BigDecimal.ZERO) == 0);

                if (allZero) {
                    System.out.println("Row " + rowNum + ": Skipped - All (entitledDays, usedDays, balance) are 0 for employeeSerialId: "
                            + employee.getEmployeeSerialId());
                    skipped++;
                    continue;
                }

                // ✅ --- DUPLICATE CHECK + CONDITIONAL UPDATE ---
                Optional<LeaveBalanceEmployee> existingOpt = balanceRepository
                        .findByEmployeeIdAndLeaveTypeIdAndYear(employee.getId(), leaveType.getId(), year);

                if (existingOpt.isPresent()) {
                    LeaveBalanceEmployee existing = existingOpt.get();

                    boolean isSame =
                            entitledDays.compareTo(existing.getEntitledDays()) == 0 &&
                                    carryForward.compareTo(existing.getCarriedForward()) == 0 &&
                                    encashed.compareTo(existing.getEncashed()) == 0 &&
                                    usedDays.compareTo(existing.getUsedDays()) == 0 &&
                                    balanceValue.compareTo(existing.getBalance()) == 0;

                    if (isSame) {
                        System.out.println("Row " + rowNum + ": Skipped - Same data already exists for employeeSerialId: "
                                + employee.getEmployeeSerialId() + ", LeaveType: " + leaveTypeName + ", Year: " + year);
                        skipped++;
                        continue;
                    }

                    // 🔄 Update existing record
                    existing.setEntitledDays(entitledDays);
                    existing.setCarriedForward(carryForward);
                    existing.setEncashed(encashed);
                    existing.setUsedDays(usedDays);
                    existing.setBalance(balanceValue);

                    balanceRepository.save(existing);
                    updated++;
                    System.out.println("Row " + rowNum + ": Updated existing record for employeeSerialId: "
                            + employee.getEmployeeSerialId() + ", LeaveType: " + leaveTypeName + ", Year: " + year);
                    continue;
                }

                // ✅ New Record
                balances.add(balance);
                inserted++;
                System.out.println("✓ Row " + rowNum + " added: EmpID=" + balance.getEmployeeId());
            }

            if (balances.isEmpty() && updated == 0) {
                System.out.println("WARNING: No leave balances to save or update!");
                return balances;
            }

            // --- Save all ---
            if (!balances.isEmpty()) {
                System.out.println("\nSaving new records to DB...");
                List<LeaveBalanceEmployee> saved = balanceRepository.saveAll(balances);
                System.out.println("✓ Saved " + saved.size() + " new records");
            }

            System.out.println("\n=== IMPORT SUMMARY ===");
            System.out.println("➕ Inserted: " + inserted);
            System.out.println("🔄 Updated: " + updated);
            System.out.println("⚪ Skipped: " + skipped);
            System.out.println("========================");

            return balances;
        }
    }


    private String getStringValue(Row row, Integer idx) {
        if (idx == null) return null;
        Cell cell = row.getCell(idx);
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> null;
        };
    }

    private Long getLongValue(Row row, Integer idx) {
        if (idx == null) return null;
        Cell cell = row.getCell(idx);
        if (cell == null) return null;
        try {
            return switch (cell.getCellType()) {
                case NUMERIC -> (long) cell.getNumericCellValue();
                case STRING -> Long.parseLong(cell.getStringCellValue().trim());
                default -> null;
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Integer getIntegerValue(Row row, Integer idx) {
        if (idx == null) return null;
        Cell cell = row.getCell(idx);
        if (cell == null) return null;
        try {
            return switch (cell.getCellType()) {
                case NUMERIC -> (int) cell.getNumericCellValue();
                case STRING -> Integer.parseInt(cell.getStringCellValue().trim());
                default -> null;
            };
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal readBigDecimalCell(Row row, Integer idx) {
        if (idx == null) return BigDecimal.ZERO;
        Cell cell = row.getCell(idx);
        if (cell == null) return BigDecimal.ZERO;

        try {
            return switch (cell.getCellType()) {
                case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING -> new BigDecimal(cell.getStringCellValue().trim());
                default -> BigDecimal.ZERO;
            };
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}
