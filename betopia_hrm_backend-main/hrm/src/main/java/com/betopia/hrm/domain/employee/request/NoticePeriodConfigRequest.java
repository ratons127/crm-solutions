package com.betopia.hrm.domain.employee.request;

public record NoticePeriodConfigRequest(
        Long employeeSeparationId,
        Integer defaultNoticeDays,
        Integer minimumNoticeDays,
        Integer gracePeriodDays,
        Boolean canWaive,
        Boolean canBuyout,
        Boolean status
) {
}
