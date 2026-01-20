package com.betopia.hrm.domain.employee.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.time.LocalDateTime;


@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "document_types")
public class DocumentType extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "category",nullable = false)
    private CategoryType category;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "is_required")
    private boolean isRequired;

    @Column(name = "is_time_bound")
    private boolean isTimeBound;

    @Column(name = "default_validity_months")
    private int defaultValidityMonths;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryType getCategory() {
        return category;
    }

    public void setCategory(CategoryType category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public boolean isTimeBound() {
        return isTimeBound;
    }

    public void setTimeBound(boolean timeBound) {
        isTimeBound = timeBound;
    }

    public int getDefaultValidityMonths() {
        return defaultValidityMonths;
    }

    public void setDefaultValidityMonths(int defaultValidityMonths) {
        this.defaultValidityMonths = defaultValidityMonths;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
