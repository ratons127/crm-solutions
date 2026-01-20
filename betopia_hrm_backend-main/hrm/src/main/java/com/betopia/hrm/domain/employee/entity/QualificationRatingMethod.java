package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "qualification_rating_methods")
public class QualificationRatingMethod extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code", nullable = false)
    private String code;
    @Column(name = "method_name")
    private String methodName;

    @OneToMany(mappedBy = "qualificationRatingMethod", cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<QualificationRatingMethodDetails> qualificationRatingMethodDetails=new ArrayList<>();

    public void addDetail(QualificationRatingMethodDetails d) {
        qualificationRatingMethodDetails.add(d);
        d.setQualificationRatingMethod(this);   // <-- FK set here
    }
    public void removeDetail(QualificationRatingMethodDetails d) {
        qualificationRatingMethodDetails.remove(d);
        d.setQualificationRatingMethod(null);
    }

    public void setQualificationRatingMethodDetails(List<QualificationRatingMethodDetails> items) {
        qualificationRatingMethodDetails.clear();
        if (items != null) items.forEach(this::addDetail);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<QualificationRatingMethodDetails> getQualificationRatingMethodDetails() {
        return qualificationRatingMethodDetails;
    }
}
