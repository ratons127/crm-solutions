package com.betopia.hrm.services.leaves.leavepolicy.leaverule.rules.request;

import com.betopia.hrm.domain.employee.entity.Employee;
import com.betopia.hrm.domain.leave.entity.LeavePolicy;
import com.betopia.hrm.domain.leave.entity.LeaveRequest;
import com.betopia.hrm.services.company.calendarsholidays.CalendarHolidaysService;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.RequestRule;
import com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine.ValidationResult;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class BridgeLeaveRule implements RequestRule {

    private final CalendarHolidaysService holidaysService;

    public BridgeLeaveRule(CalendarHolidaysService holidaysService) {
        this.holidaysService = holidaysService;
    }

    @Override
    public ValidationResult validate(Employee employee, LeaveRequest request, LeavePolicy policy) {
        if (policy.isRestrictBridgeLeave()) {

            // get all dates between start & end
            List<LocalDate> range = request.getStartDate()
                    .datesUntil(request.getEndDate().plusDays(1))
                    .toList();

            // check holidays/weekends in between
            long bridgeDays = range.stream()
                    .filter(holidaysService::isHolidayOrWeekend)
                    .count();

            if (bridgeDays > policy.getMaxBridgeDays()) {
                return ValidationResult.fail(
                        "BRIDGE_LEAVE_NOT_ALLOWED",
                        "Leave request bridges " + bridgeDays + " holidays/weekends. Allowed max = " + policy.getMaxBridgeDays()
                );
            }
        }
        return ValidationResult.ok();
    }
}
