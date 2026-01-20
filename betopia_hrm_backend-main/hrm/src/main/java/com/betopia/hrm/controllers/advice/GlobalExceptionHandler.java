package com.betopia.hrm.controllers.advice;


import com.betopia.hrm.domain.admin.exceptions.CountryNotFound;
import com.betopia.hrm.domain.admin.exceptions.LocationAlreadyExist;
import com.betopia.hrm.domain.admin.exceptions.LocationNotFound;

import com.betopia.hrm.domain.attendance.exception.*;
import com.betopia.hrm.domain.company.exception.CalendarsAlreadyExistException;
import com.betopia.hrm.domain.company.exception.CalendarsNotFoundException;
import com.betopia.hrm.domain.company.exception.DepartmentAlreadyExistException;
import com.betopia.hrm.domain.company.exception.DepartmentNotFoundException;
import com.betopia.hrm.domain.company.exception.NotificationBindingsAlreadyExistException;
import com.betopia.hrm.domain.company.exception.NotificationBindingsNotFoundException;
import com.betopia.hrm.domain.company.exception.NotificationEventsAlreadyExistException;
import com.betopia.hrm.domain.company.exception.NotificationEventsNotFoundException;
import com.betopia.hrm.domain.company.exception.NotificationProvidersAlreadyExistException;
import com.betopia.hrm.domain.company.exception.NotificationProvidersNotFoundException;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeAreadyExist;
import com.betopia.hrm.domain.employee.exception.employee.EmployeeNotFound;
import com.betopia.hrm.domain.employee.exception.employeeTypes.EmployeeTypesAlreadyExist;
import com.betopia.hrm.domain.employee.exception.employeeTypes.EmployeeTypesNotFound;
import com.betopia.hrm.domain.employee.exception.employeeeducationinfo.EmployeeEducationInfoAlreadyExistException;
import com.betopia.hrm.domain.employee.exception.employeeeducationinfo.EmployeeEducationInfoNotFoundException;
import com.betopia.hrm.domain.employee.exception.employeeworkexperience.EmployeeWorkExperienceAlreadyExist;
import com.betopia.hrm.domain.employee.exception.employeeworkexperience.EmployeeWorkExperienceNotFoundException;
import com.betopia.hrm.domain.employee.exception.employmentstatus.EmploymentStatusAlreadyExistException;
import com.betopia.hrm.domain.employee.exception.employmentstatus.EmploymentStatusNotFoundException;
import com.betopia.hrm.domain.employee.exception.employmentstatushistory.EmploymentStatusHistoryAlreadyExist;
import com.betopia.hrm.domain.employee.exception.employmentstatushistory.EmploymentStatusHistoryNotFound;
import com.betopia.hrm.domain.employee.exception.fieldStudy.FieldStudyAlreadyException;
import com.betopia.hrm.domain.employee.exception.fieldStudy.FieldStudyNotFoundException;
import com.betopia.hrm.domain.employee.exception.grade.GradeAlreadyExistException;
import com.betopia.hrm.domain.employee.exception.grade.GradeNotFoundException;
import com.betopia.hrm.domain.employee.exception.instituteName.InstituteNameAlreadyExistException;
import com.betopia.hrm.domain.employee.exception.instituteName.InstituteNameNotFoundException;
import com.betopia.hrm.domain.employee.exception.personalHobby.PersonalHobbyAlreadyExistException;
import com.betopia.hrm.domain.employee.exception.personalHobby.PersonalHobbyNotFoundException;
import com.betopia.hrm.domain.employee.exception.qualificationLevel.QualificationLevelAlreadyExist;
import com.betopia.hrm.domain.employee.exception.qualificationLevel.QualificationLevelNotFoundException;
import com.betopia.hrm.domain.employee.exception.qualificationRatingMethod.QualificationRatingMethodAlreadyExists;
import com.betopia.hrm.domain.employee.exception.qualificationRatingMethod.QualificationRatingMethodNotFound;
import com.betopia.hrm.domain.employee.exception.qualificationType.QualificationTypeAlreadyExist;
import com.betopia.hrm.domain.employee.exception.qualificationType.QualificationTypeNotFoundException;
import com.betopia.hrm.domain.leave.exception.leaveCategory.LeaveCategoryNotFoundException;
import com.betopia.hrm.domain.leave.exception.leaveGroupAssign.LeaveGroupAssignNotFoundException;
import com.betopia.hrm.domain.leave.exception.leavebalanceemployee.LeaveBalanceEmployeeAlreadyExistException;
import com.betopia.hrm.domain.leave.exception.leavebalanceemployee.LeaveBalanceEmployeeNotFoundException;
import com.betopia.hrm.domain.leave.exception.leaveeligibilityrules.LeaveEligibilityRulesAlreadyExistException;
import com.betopia.hrm.domain.leave.exception.leaveeligibilityrules.LeaveEligibilityRulesNotFoundException;

import com.betopia.hrm.domain.leave.exception.leavegroup.LeaveGroupAlreadyExistException;
import com.betopia.hrm.domain.leave.exception.leavegroup.LeaveGroupNotFoundException;
import com.betopia.hrm.domain.leave.exception.leavepolicy.LeavePolicyAlreadyExistException;
import com.betopia.hrm.domain.leave.exception.leavepolicy.LeavePolicyNotFoundException;
import com.betopia.hrm.domain.leave.exception.leavepolicy.ValidationException;
import com.betopia.hrm.domain.leave.exception.leavetype.LeaveTypeAlreadyExistException;
import com.betopia.hrm.domain.leave.exception.leavetype.LeaveTypeNotFoundException;
import com.betopia.hrm.domain.leave.exception.leaveyear.LeaveYearAlreadyExistException;
import com.betopia.hrm.domain.leave.exception.leaveyear.LeaveYearNotFoundException;
import com.betopia.hrm.domain.lookup.exception.lookupsetupdetails.LookupSetupDetailsAlreadyExistExeption;
import com.betopia.hrm.domain.lookup.exception.lookupsetupdetails.LookupSetupDetailsNotFoundException;
import com.betopia.hrm.domain.lookup.exception.lookupsetupentry.LookupSetupEntryAlreadyExistExeption;
import com.betopia.hrm.domain.lookup.exception.lookupsetupentry.LookupSetupEntryNotFoundExeption;
import com.betopia.hrm.domain.users.exception.menu.MenuNotFoundException;
import com.betopia.hrm.domain.users.exception.user.InvalidPasswordException;
import com.betopia.hrm.domain.users.exception.workplace.WorkplaceNotFound;
import com.betopia.hrm.domain.users.exception.company.CompanyNotFound;
import com.betopia.hrm.domain.users.exception.role.RoleAlreadyExistException;
import com.betopia.hrm.domain.users.exception.role.RoleNotFoundException;
import com.betopia.hrm.domain.users.exception.user.PasswordPolicyNotFound;
import com.betopia.hrm.domain.users.exception.user.UsernameNotFoundException;
import com.betopia.hrm.domain.base.response.GlobalResponse;
import com.betopia.hrm.domain.base.response.ValidationResponse;
import com.betopia.hrm.domain.workflow.exceptions.ApprovalActionNotFound;
import com.betopia.hrm.domain.workflow.exceptions.ModuleNotFound;
import com.betopia.hrm.domain.workflow.exceptions.StageApproverNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowAuditLogNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowEscalationNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceAssigneeNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowInstanceStageNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowNotificationNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowStageNotFound;
import com.betopia.hrm.domain.workflow.exceptions.WorkflowTemplateNotFound;
import org.apache.coyote.BadRequestException;
import org.hibernate.JDBCException;
import org.hibernate.exception.SQLGrammarException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<GlobalResponse> handleRuntimeException(RuntimeException exception) {
        String message = getExactMessage(exception, "Unexpected runtime error occurred", false);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, "RuntimeException", null);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<GlobalResponse> nullPointException(NullPointerException exception) {
        String message = getExactMessage(exception, "Null reference found", false);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, message, "NullPointerException", null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GlobalResponse> handleIllegalArgument(IllegalArgumentException ex) {
        String message = getExactMessage(ex, "Invalid argument provided", false);
        return buildResponse(HttpStatus.BAD_REQUEST, message, "IllegalArgumentException", null);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GlobalResponse> handleBadRequest(BadRequestException ex) {
        String message = getExactMessage(ex, "Bad request", false);
        return ResponseEntity
                .badRequest()
                .body(new GlobalResponse(
                        List.of(message),
                        null,
                        message,
                        false,
                        400
                ));
    }

    // Generic handler for duplicate key / unique constraint
    /*@ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Map<String, String>> handleDuplicateKeyException(DataIntegrityViolationException ex) {

        String message = "Duplicate key error"; // default message

        // Hibernate specific message extraction
        if (ex.getRootCause() != null) {
            String rootMsg = ex.getRootCause().getMessage();
            if (rootMsg != null && rootMsg.toLowerCase().contains("duplicate")) {
                message = rootMsg; // show actual duplicate key message
            }
        }

        Map<String, String> errors = new HashMap<>();
        errors.put("message", message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }*/

    @ExceptionHandler({
            RoleAlreadyExistException.class,
            LeaveTypeAlreadyExistException.class,
            LeaveGroupAlreadyExistException.class,
            LeaveYearAlreadyExistException.class,

            LocationAlreadyExist.class,
            LeaveEligibilityRulesAlreadyExistException.class,
            LeaveBalanceEmployeeAlreadyExistException.class,

            LookupSetupEntryAlreadyExistExeption.class,
            LookupSetupDetailsAlreadyExistExeption.class,
            LeaveBalanceEmployeeAlreadyExistException.class,
            LeavePolicyAlreadyExistException.class,

            DepartmentAlreadyExistException.class,
            CalendarsAlreadyExistException.class,

            NotificationProvidersAlreadyExistException.class,
            NotificationEventsAlreadyExistException.class,
            NotificationBindingsAlreadyExistException.class,

            EmploymentStatusAlreadyExistException.class,
            EmploymentStatusHistoryAlreadyExist.class,

            EmployeeAreadyExist.class,
            EmployeeTypesAlreadyExist.class,
            QualificationRatingMethodAlreadyExists.class,
            GradeAlreadyExistException.class,
            FieldStudyAlreadyException.class,
            InstituteNameAlreadyExistException.class,
            PersonalHobbyAlreadyExistException.class,
            QualificationLevelAlreadyExist.class,
            QualificationTypeAlreadyExist.class,
            EmployeeEducationInfoAlreadyExistException.class,
            EmployeeWorkExperienceAlreadyExist.class
    })
    public ResponseEntity<Map<String, String>> handleAlreadyExistException(RuntimeException exception) {
        log.warn("Entity already exists: {}", exception.getMessage());

        Map<String, String> errors = new HashMap<>();
        String message = getExactMessage(exception, "Resource already exists", false);
        errors.put("message", message);

        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler({
            WorkplaceNotFound.class,
            CompanyNotFound.class,
            RoleNotFoundException.class,
            UsernameNotFoundException.class,

            PasswordPolicyNotFound.class,

            LeaveTypeNotFoundException.class,
            LeaveGroupNotFoundException.class,
            LeaveYearNotFoundException.class,

            CountryNotFound.class,
            LocationNotFound.class,

            LeaveTypeNotFoundException.class,
            LeaveEligibilityRulesNotFoundException.class,
            LeaveBalanceEmployeeNotFoundException.class,

            LookupSetupEntryNotFoundExeption.class,
            LookupSetupDetailsNotFoundException.class,
            LeaveBalanceEmployeeNotFoundException.class,
            LeavePolicyNotFoundException.class,

            DepartmentNotFoundException.class,
            CalendarsNotFoundException.class,
            MenuNotFoundException.class,

            NotificationProvidersNotFoundException.class,
            NotificationEventsNotFoundException.class,
            NotificationBindingsNotFoundException.class,
            LeaveGroupAssignNotFoundException.class,
            LeaveCategoryNotFoundException.class,

            EmploymentStatusNotFoundException.class,
            EmploymentStatusHistoryNotFound.class,

            EmployeeNotFound.class,
            EmployeeTypesNotFound.class,
            QualificationRatingMethodNotFound.class,
            GradeNotFoundException.class,
            FieldStudyNotFoundException.class,
            InstituteNameNotFoundException.class,
            PersonalHobbyNotFoundException.class,
            QualificationLevelNotFoundException.class,
            QualificationTypeNotFoundException.class,
            EmployeeEducationInfoNotFoundException.class,
            EmployeeWorkExperienceNotFoundException.class,
            ShiftCategoryNotFoundException.class,
            ShiftNotFoundException.class,
            ShiftRotationPatternNotFoundException.class,
            ShiftEmployeeRotationNotFoundException.class,
            AttendanceDeviceCategoryNotFoundException.class,
            AttendanceDeviceNotFoundException.class,
            AttendanceDeviceAssignNotFoundException.class,
            WorkflowTemplateNotFound.class,
            ModuleNotFound.class,
            WorkflowStageNotFound.class,
            StageApproverNotFound.class,
            WorkflowInstanceNotFound.class,
            WorkflowInstanceStageNotFound.class,
            ApprovalActionNotFound.class,
            WorkflowNotificationNotFound.class,
            WorkflowEscalationNotFound.class,
            WorkflowAuditLogNotFound.class,
            WorkflowInstanceAssigneeNotFound.class,
            InvalidPasswordException.class
    })
    public ResponseEntity<Map<String, String>> handleNotFoundException(RuntimeException exception) {
        log.warn("Entity not found: {}", exception.getMessage());

        Map<String, String> errors = new HashMap<>();
        String message = getExactMessage(exception, "Resource not found", false);
        errors.put("message", message);

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationResponse> handleValidationExceptions(MethodArgumentNotValidException exception)
    {
        Map<String, Object> errors = new HashMap<>();

        exception.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        ValidationResponse response = new ValidationResponse(
                errors,
                "validation error",
                HttpStatus.UNPROCESSABLE_ENTITY.value()

        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GlobalResponse> handleAll(Exception ex) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong", ex.getClass().getSimpleName(), null);
    }

    private ResponseEntity<GlobalResponse> buildResponse(HttpStatus status, String message, String error, Object data)
    {
        GlobalResponse response = new GlobalResponse(
                Collections.singletonList(error), // errors
                data,                             // data
                message,                          // message
                false,                            // success
                status.value()                    // status
        );

        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(ValidationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("errorCode", ex.getErrorCode());
        response.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    private String getExactMessage(Exception ex, String defaultMessage, boolean firstLineOnly) {
        if (ex.getMessage() != null && !ex.getMessage().isBlank()) {
            if (firstLineOnly) {
                String[] parts = ex.getMessage().split("\\r?\\n");
                return parts[0].trim();
            }
            return ex.getMessage().trim();
        }
        return defaultMessage;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        Throwable root = getRootCause(ex);
        String message = root != null ? root.getMessage() : ex.getMessage();

        String constraint = extractConstraintName(message);
        String userMessage = null;

        if (message != null) {
            String lowerMsg = message.toLowerCase();

            // Handle duplicate key / unique constraint
            if (lowerMsg.contains("duplicate key value") || lowerMsg.contains("unique constraint")) {
                String fieldName = extractFieldFromConstraint(constraint);
                userMessage = (fieldName != null)
                        ? String.format("The %s already exists.", fieldName)
                        : "Duplicate value detected. Please use a unique value.";
            }

            // Handle foreign key violations
            else if (lowerMsg.contains("violates foreign key constraint")) {
                userMessage = ForeignKeyMessages.getMessage(constraint);
            }

            // Value too long for column (varchar length exceeded)
            else if (lowerMsg.contains("value too long for type character varying")) {
                userMessage = extractLengthViolationMessage(lowerMsg);
            }

            // Generic fallback
            else {
                userMessage = "A data integrity error occurred. Please check your input.";
            }
        }

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ApiError(HttpStatus.CONFLICT.value(), userMessage));
    }

    private String extractConstraintName(String msg) {
        if (msg == null) return null;
        Matcher matcher = Pattern.compile("constraint\\s+\"([^\"]+)\"", Pattern.CASE_INSENSITIVE).matcher(msg);
        return matcher.find() ? matcher.group(1) : null;
    }

    private String extractLengthViolationMessage(String lowerMsg) {
        Matcher m = Pattern.compile("character varying\\((\\d+)\\)").matcher(lowerMsg);
        if (m.find()) {
            String maxLength = m.group(1);
            return String.format("Input value exceeds the maximum allowed length (%s characters).", maxLength);
        }
        return "Input value exceeds allowed field length.";
    }

    private String extractFieldFromConstraint(String constraint) {
        if (constraint == null) return null;
        String[] parts = constraint.split("_");
        if (parts.length >= 2) return parts[1];
        return null;
    }

    private Throwable getRootCause(Throwable ex) {
        return (ex.getCause() != null) ? getRootCause(ex.getCause()) : ex;
    }

    @ExceptionHandler({ SQLGrammarException.class, JDBCException.class })
    public ResponseEntity<ApiError> handleSQLGrammarException(JDBCException ex) {
        String message = ex.getMessage();
        String userMessage = "An internal database error occurred. Please contact support.";

        if (message != null && message.toLowerCase().contains("does not exist")) {
            // More specific for "column ... does not exist"
            userMessage = "A system configuration error occurred. Please contact the system administrator.";
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiError(HttpStatus.INTERNAL_SERVER_ERROR.value(), userMessage));
    }

    /*
    * The controller expects a @RequestBody,
    *  but the client sends no body or sends invalid/malformed JSON.
    * */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String userMessage = "Request body is missing or malformed. Please check your input.";
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiError(HttpStatus.BAD_REQUEST.value(), userMessage));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of(
                        "message", "Invalid username or password",
                        "success", false,
                        "status", HttpStatus.UNAUTHORIZED.value()
                ));
    }
}
