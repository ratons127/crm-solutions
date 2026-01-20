package com.betopia.hrm.domain.employee.request;

import com.betopia.hrm.domain.employee.enums.ConfidentialityLevel;
import com.betopia.hrm.domain.employee.enums.ExitInterviewStatus;
import com.betopia.hrm.domain.employee.enums.InterviewMode;

import java.time.LocalDateTime;
import java.util.Map;

public record ExitInterviewRequest(
        Long separationId,
        InterviewMode interviewMode,
        LocalDateTime scheduledDate,
        LocalDateTime completedDate,
        Long interviewerId,
        Integer overallSatisfactionRating,
        Integer workEnvironmentRating,
        Integer managementRating,
        Integer growthOpportunityRating,
        Integer compensationRating,
        String primaryReasonLeaving,
        Boolean wouldRecommend,
        Boolean wouldRejoin,
        String additionalComments,
        ExitInterviewStatus exitStatus,
        ConfidentialityLevel confidentialityLevel,
        Map<String, Object> interviewData
) {
}
