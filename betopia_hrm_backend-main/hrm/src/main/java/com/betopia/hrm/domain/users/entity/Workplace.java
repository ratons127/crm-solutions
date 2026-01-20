package com.betopia.hrm.domain.users.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.company.entity.WorkplaceGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.Objects;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "workplaces")
public class Workplace extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "workplace_group_id", nullable = false)
    private WorkplaceGroup workplaceGroup;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "code", length = 50)
    private String code;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "status")
    private Boolean status ;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public WorkplaceGroup getWorkplaceGroup() {
        return workplaceGroup;
    }

    public void setWorkplaceGroup(WorkplaceGroup workplaceGroup) {
        this.workplaceGroup = workplaceGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Workplace workplace = (Workplace) o;
        return Objects.equals(id, workplace.id) && Objects.equals(name, workplace.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
