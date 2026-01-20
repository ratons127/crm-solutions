package com.betopia.hrm.domain.employee.request;


import com.betopia.hrm.domain.employee.enums.DocumentExpiryAlertStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record DocumentExpiryAlertRequest(

        Long id,

        @NotNull(message = "Document Id cannot be blank")
        Long employeeDocumentId,

        LocalDate alertDate,

        @NotNull(message = "User Id cannot be blank")
        Long userId,

        LocalDateTime sentAt,
        DocumentExpiryAlertStatus status,
        LocalDateTime deletedAt

) {

}