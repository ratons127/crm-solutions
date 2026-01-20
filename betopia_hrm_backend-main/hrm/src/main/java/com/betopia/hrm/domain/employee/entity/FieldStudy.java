package com.betopia.hrm.domain.employee.entity;


import com.betopia.hrm.domain.base.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "field_study")
public class FieldStudy extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "qualification_level_id", nullable = false)
    private Long qualificationLevelId;

    @Column(name = "field_study_name", nullable = false)
    private String fieldStudyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getQualificationLevelId() {
        return qualificationLevelId;
    }

    public void setQualificationLevelId(Long qualificationLevelId) {
        this.qualificationLevelId = qualificationLevelId;
    }

    public String getFieldStudyName() {
        return fieldStudyName;
    }

    public void setFieldStudyName(String fieldStudyName) {
        this.fieldStudyName = fieldStudyName;
    }
}
