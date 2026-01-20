package com.betopia.hrm.domain.employee.entity;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "employee_education_info")
public class EmployeeEducationInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "employee_id", updatable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "qualification_type_id", updatable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private QualificationType qualificationType;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "qualification_level_id", updatable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private QualificationLevel qualificationLevel;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "institute_name_id", updatable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private InstituteName instituteName;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "field_study_id", updatable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private FieldStudy fieldStudy;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "qualification_rating_method_id", updatable = false)
    @JsonIdentityReference(alwaysAsId = true)
    private QualificationRatingMethod qualificationRatingMethod;

    @Column(name="subject")
    private String subject;

    @Column(name="result")
    private String result;

    @Column(name="passing_year")
    private Integer passingYear;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public QualificationType getQualificationType() {
        return qualificationType;
    }

    public void setQualificationType(QualificationType qualificationType) {
        this.qualificationType = qualificationType;
    }

    public QualificationLevel getQualificationLevel() {
        return qualificationLevel;
    }

    public void setQualificationLevel(QualificationLevel qualificationLevel) {
        this.qualificationLevel = qualificationLevel;
    }

    public InstituteName getInstituteName() {
        return instituteName;
    }

    public void setInstituteName(InstituteName instituteName) {
        this.instituteName = instituteName;
    }

    public FieldStudy getFieldStudy() {
        return fieldStudy;
    }

    public void setFieldStudy(FieldStudy fieldStudy) {
        this.fieldStudy = fieldStudy;
    }

    public QualificationRatingMethod getQualificationRatingMethod() {
        return qualificationRatingMethod;
    }

    public void setQualificationRatingMethod(QualificationRatingMethod qualificationRatingMethod) {
        this.qualificationRatingMethod = qualificationRatingMethod;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public Integer getPassingYear() {
        return passingYear;
    }

    public void setPassingYear(Integer passingYear) {
        this.passingYear = passingYear;
    }
}
