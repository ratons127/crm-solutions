package com.betopia.hrm.domain.employee.exception.promotionrequests;

public class PromotionRequestsAlreadyExistException extends RuntimeException {
    public PromotionRequestsAlreadyExistException(String message) {
        super(message);
    }
}
