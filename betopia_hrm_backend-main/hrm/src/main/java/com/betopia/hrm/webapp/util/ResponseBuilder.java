package com.betopia.hrm.webapp.util;

import com.betopia.hrm.domain.base.response.GlobalResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder {

    public static <T> ResponseEntity<GlobalResponse> ok(T data, String message) {
        return ResponseEntity.ok(
            GlobalResponse.success(data, message, HttpStatus.OK.value())
        );
    }

    public static <T> ResponseEntity<GlobalResponse> created(T data, String message) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(GlobalResponse.success(data, message, HttpStatus.CREATED.value()));
    }

    public static ResponseEntity<GlobalResponse> noContent(String message) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
            .body(GlobalResponse.success(null, message, HttpStatus.NO_CONTENT.value()));
    }

    public static ResponseEntity<GlobalResponse> badRequest(String message) {
        return ResponseEntity.badRequest()
                .body(GlobalResponse.error(null, message, HttpStatus.BAD_REQUEST.value()));
    }

    public static ResponseEntity<GlobalResponse> error(String message, HttpStatus status) {
        return ResponseEntity.status(status)
                .body(GlobalResponse.error(null, message, status.value()));
    }
}
