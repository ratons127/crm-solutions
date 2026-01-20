package com.betopia.hrm.domain.employee.request;

import java.math.BigDecimal;

public record EmployeeWorkExperienceRequest(
        Long employeeId,
        String companyName,
        String jobTitle,
        String location,
        String jobDescription,
        java.time.LocalDate fromDate,
        java.time.LocalDate toDate,
        BigDecimal tenure,
        String image,
        String imageUrl
) {
}
