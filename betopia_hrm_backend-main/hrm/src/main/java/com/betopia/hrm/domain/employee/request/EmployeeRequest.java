package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.lookup.entity.LookupSetupDetails;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record EmployeeRequest(
         @NotBlank(message = "Name can not be blank")
         @NotNull(message = "First name can not be null")
         String firstName,
         String lastName,
         String gender,
         @NotNull(message = "Joining date can not be null")
         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
         LocalDate dateOfJoining,
         @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
         LocalDate dob,
         String photo,
         String imageUrl,
         String nationalId,
         @NotNull(message = "Phone can not be null")
         String phone,
         @NotNull(message = "Email name can not be null")
         String email,
         String presentAddress,
         String permanentAddress,
         String maritalStatus,
         String emergencyContactName,
         String emergencyContactRelation,
         String emergencyContactPhone,
         Long employeeTypeId,
         Long departmentId,
         Long designationId,
         Long supervisorId,
         Long workPlaceId,
         Long businessUnitId,
         Long workPlaceGroupId,
         Long gradeId,
         Long lineManagerId,
         Long companyId,
         String jobTitle,
         Long roleId,
         Long teamId,
         String deviceUserId,
         BigDecimal grossSalary,
         Long religionId,
         Long nationalityId,
         Long bloodGroupId,
         Long paymentTypeId,
         Long probationDurationId,
         String birthCertificateNumber,
         String passportNumber,
         String drivingLicenseNumber,
         String tinNumber,
         String officePhone,
         String officeEmail,

         LocalDateTime estimatedConfirmationDate,
         LocalDateTime actualConfirmationDate
        // @Pattern(regexp = "^[0-9]+$", message = "Only numbers are allowed")
         //String employeeSerialId
) {
}
