package com.betopia.hrm.domain.leave.entity;

import com.betopia.hrm.domain.base.annotation.ExposeField;
import com.betopia.hrm.domain.base.entity.Auditable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "leave_categories")
public class LeaveCategory extends Auditable<Long, Long> {

    @ExposeField
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ExposeField
    @Column(length = 255, nullable = false, unique = true, name = "name")
    private String name;

    // parent category for subcategories
    @ExposeField
    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private LeaveCategory parent;

    @ExposeField
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LeaveCategory> subCategories = new ArrayList<>();

    @ExposeField
    @Column(length = 10, nullable = false)
    private Boolean status = true;

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

    public LeaveCategory getParent() {
        return parent;
    }

    public void setParent(LeaveCategory parent) {
        this.parent = parent;
    }


    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
