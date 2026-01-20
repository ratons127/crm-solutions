package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.ConfidentialityLevel;
import com.betopia.hrm.domain.employee.enums.ExitInterviewStatus;
import com.betopia.hrm.domain.employee.enums.InterviewMode;
import com.betopia.hrm.domain.users.entity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.Map;

@Entity
@Table(name = "exit_interview")
public class ExitInterview extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "separation_id")
    private EmployeeSeparations separation;

    @Enumerated(EnumType.STRING)
    @Column(name = "interview_mode")
    private InterviewMode interviewMode;

    @Column(name = "scheduled_date")
    private LocalDateTime scheduledDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interviewer_id")
    private User interviewer;

    // Ratings (1–5)
    @Column(name = "overall_satisfaction_rating")
    private Integer overallSatisfactionRating;

    @Column(name = "work_environment_rating")
    private Integer workEnvironmentRating;

    @Column(name = "management_rating")
    private Integer managementRating;

    @Column(name = "growth_opportunity_rating")
    private Integer growthOpportunityRating;

    @Column(name = "compensation_rating")
    private Integer compensationRating;

    @Column(name = "primary_reason_leaving", length = 500)
    private String primaryReasonLeaving;

    @Column(name = "would_recommend")
    private Boolean wouldRecommend;

    @Column(name = "would_rejoin")
    private Boolean wouldRejoin;

    @Column(name = "additional_comments", columnDefinition = "TEXT")
    private String additionalComments;

    @Enumerated(EnumType.STRING)
    @Column(name = "exit_status")
    private ExitInterviewStatus exitStatus ;

    @Enumerated(EnumType.STRING)
    @Column(name = "confidentiality_level")
    private ConfidentialityLevel confidentialityLevel;

    @Column(name = "interview_data", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> interviewData;


    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeSeparations getSeparation() {
        return separation;
    }

    public void setSeparation(EmployeeSeparations separation) {
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

    public User getInterviewer() {
        return interviewer;
    }

    public void setInterviewer(User interviewer) {
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

    public Map<String, Object> getInterviewData() { return interviewData;
    }
    public void setInterviewData(Map<String, Object> interviewData) { this.interviewData = interviewData;
    }
}
