package com.betopia.hrm.domain.employee.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "qualification_rating_methods_details")
public class QualificationRatingMethodDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade", nullable = false)
    private String grade;

    @Column(name = "maximum_mark", nullable = false)
    private Integer maximumMark;

    @Column(name = "minimum_mark", nullable = false)
    private Integer minimumMark;

    @Column(name = "average_mark", nullable = false)
    private Integer averageMark;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "qualification_rating_methods_id", referencedColumnName = "id", nullable = false)
    @JsonBackReference
    private QualificationRatingMethod qualificationRatingMethod;

    public QualificationRatingMethodDetails(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Integer getMaximumMark() {
        return maximumMark;
    }

    public void setMaximumMark(Integer maximumMark) {
        this.maximumMark = maximumMark;
    }

    public Integer getMinimumMark() {
        return minimumMark;
    }

    public void setMinimumMark(Integer minimumMark) {
        this.minimumMark = minimumMark;
    }

    public Integer getAverageMark() {
        return averageMark;
    }

    public void setAverageMark(Integer averageMark) {
        this.averageMark = averageMark;
    }

    public QualificationRatingMethod getQualificationRatingMethod() {
        return qualificationRatingMethod;
    }

    public void setQualificationRatingMethod(QualificationRatingMethod qualificationRatingMethod) {
        this.qualificationRatingMethod = qualificationRatingMethod;
    }
}
