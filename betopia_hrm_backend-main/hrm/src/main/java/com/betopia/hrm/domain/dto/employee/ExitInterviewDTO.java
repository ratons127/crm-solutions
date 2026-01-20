package com.betopia.hrm.domain.dto.employee;

import com.betopia.hrm.domain.dto.user.UserDTO;
import com.betopia.hrm.domain.employee.enums.ConfidentialityLevel;
import com.betopia.hrm.domain.employee.enums.ExitInterviewStatus;
import com.betopia.hrm.domain.employee.enums.InterviewMode;

import java.time.LocalDateTime;
import java.util.Map;

public class ExitInterviewDTO {

    private Long id;
    private EmployeeSeparationsDTO separation;
    private InterviewMode interviewMode;
    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;
    private UserDTO interviewer;
    private Integer overallSatisfactionRating;
    private Integer workEnvironmentRating;
    private Integer managementRating;
    private Integer growthOpportunityRating;
    private Integer compensationRating;
    private String primaryReasonLeaving;
    private Boolean wouldRecommend;
    private Boolean wouldRejoin;
    private String additionalComments;
    private ExitInterviewStatus exitStatus;
    private ConfidentialityLevel confidentialityLevel;
    private Map<String, Object> interviewData;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeSeparationsDTO getSeparation() {
        return separation;
    }

    public void setSeparation(EmployeeSeparationsDTO separation) {
        this.separation = separation;
    }

    public InterviewMode getInterviewMode() {
        return interviewMode;
    }

    public void setInterviewMode(InterviewMode interviewMode) {
        this.interviewMode = interviewMode;
    }

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }

    public LocalDateTime getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(LocalDateTime completedDate) {
        this.completedDate = completedDate;
    }

    public UserDTO getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(UserDTO interviewer) {
        this.interviewer = interviewer;
    }

    public Integer getOverallSatisfactionRating() {
        return overallSatisfactionRating;
    }

    public void setOverallSatisfactionRating(Integer overallSatisfactionRating) {
        this.overallSatisfactionRating = overallSatisfactionRating;
    }

    public Integer getWorkEnvironmentRating() {
        return workEnvironmentRating;
    }

    public void setWorkEnvironmentRating(Integer workEnvironmentRating) {
        this.workEnvironmentRating = workEnvironmentRating;
    }

    public Integer getManagementRating() {
        return managementRating;
    }

    public void setManagementRating(Integer managementRating) {
        this.managementRating = managementRating;
    }

    public Integer getGrowthOpportunityRating() {
        return growthOpportunityRating;
    }

    public void setGrowthOpportunityRating(Integer growthOpportunityRating) {
        this.growthOpportunityRating = growthOpportunityRating;
    }

    public Integer getCompensationRating() {
        return compensationRating;
    }

    public void setCompensationRating(Integer compensationRating) {
        this.compensationRating = compensationRating;
    }

    public String getPrimaryReasonLeaving() {
        return primaryReasonLeaving;
    }

    public void setPrimaryReasonLeaving(String primaryReasonLeaving) {
        this.primaryReasonLeaving = primaryReasonLeaving;
    }

    public Boolean getWouldRecommend() {
        return wouldRecommend;
    }

    public void setWouldRecommend(Boolean wouldRecommend) {
        this.wouldRecommend = wouldRecommend;
    }

    public Boolean getWouldRejoin() {
        return wouldRejoin;
    }

    public void setWouldRejoin(Boolean wouldRejoin) {
        this.wouldRejoin = wouldRejoin;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    public ExitInterviewStatus getExitStatus() {
        return exitStatus;
    }

    public void setExitStatus(ExitInterviewStatus exitStatus) {
        this.exitStatus = exitStatus;
    }

    public ConfidentialityLevel getConfidentialityLevel() {
        return confidentialityLevel;
    }

    public void setConfidentialityLevel(ConfidentialityLevel confidentialityLevel) {
        this.confidentialityLevel = confidentialityLevel;
    }

    public Map<String, Object> getInterviewData() { return interviewData; }
    public void setInterviewData(Map<String, Object> interviewData) { this.interviewData = interviewData; }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
