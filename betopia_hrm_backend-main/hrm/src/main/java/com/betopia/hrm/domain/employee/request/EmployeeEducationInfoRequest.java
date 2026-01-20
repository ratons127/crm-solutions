package com.betopia.hrm.domain.employee.request;

public record EmployeeEducationInfoRequest(Long employeeId,
                                           Long qualificationTypeId,
                                           Long qualificationLevelId,
                                           Long instituteNameId,
                                           Long fieldStudyId,
                                           Long qualificationRatingMethodId,
                                           String subject,
                                           String result,
                                           Integer passingYear) {
}
