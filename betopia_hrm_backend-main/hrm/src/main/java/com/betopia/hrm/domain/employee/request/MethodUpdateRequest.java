package com.betopia.hrm.domain.employee.request;

import java.util.List;

public record MethodUpdateRequest(String code,
                                  String methodName,
                                  List<DetailDto> qualificationRatingMethodDetails) {
}
