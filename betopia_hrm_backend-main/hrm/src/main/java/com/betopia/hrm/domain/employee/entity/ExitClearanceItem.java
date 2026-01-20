package com.betopia.hrm.domain.employee.entity;


import com.betopia.hrm.domain.base.entity.Auditable;
import com.betopia.hrm.domain.employee.enums.Departments;
import jakarta.persistence.*;

@Entity
@Table(name = "exit_clearance_items")
public class ExitClearanceItem extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "template_id", nullable = false)
    private ExitClearanceTemplate template;

    @Enumerated(EnumType.STRING)
    @Column(name = "department", nullable = false, length = 50)
    private Departments department;

    @Column(name = "item_name", nullable = false, length = 200)
    private String itemName;

    @Column(name = "item_description", columnDefinition = "TEXT")
    private String itemDescription;

    @Column(name = "is_mandatory", nullable = false)
    private Boolean isMandatory = true;

    @Column(name = "assignee_role", length = 100)
    private String assigneeRole;

    @Column(name = "sequence_order")
    private Integer sequenceOrder;

    @Column(name = "status", nullable = false)
    private Boolean status = true;


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ExitClearanceTemplate getTemplate() { return template; }
    public void setTemplate(ExitClearanceTemplate template) { this.template = template; }

    public Departments getDepartment() { return department; }
    public void setDepartment(Departments department) { this.department = department; }

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }

    public String getItemDescription() { return itemDescription; }
    public void setItemDescription(String itemDescription) { this.itemDescription = itemDescription; }

    public Boolean getIsMandatory() { return isMandatory; }
    public void setIsMandatory(Boolean isMandatory) { this.isMandatory = isMandatory; }

    public String getAssigneeRole() { return assigneeRole; }
    public void setAssigneeRole(String assigneeRole) { this.assigneeRole = assigneeRole; }

    public Integer getSequenceOrder() { return sequenceOrder; }
    public void setSequenceOrder(Integer sequenceOrder) { this.sequenceOrder = sequenceOrder; }

    public Boolean getStatus() { return status; }
    public void setStatus(Boolean isActive) { this.status = status; }
}
