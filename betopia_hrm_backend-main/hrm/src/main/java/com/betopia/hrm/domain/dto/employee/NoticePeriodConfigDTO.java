package com.betopia.hrm.domain.dto.employee;

public class NoticePeriodConfigDTO {

    private Long id;
    private String name;
    private String type;
    private EmployeeSeparationsDTO employeeSeparation;
    Integer defaultNoticeDays;
    Integer minimumNoticeDays;
    Integer gracePeriodDays;
    Boolean canWaive;
    Boolean canBuyout;
    Boolean status;

    // Constructors
    public NoticePeriodConfigDTO() {
    }

    // Getters and Setters
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EmployeeSeparationsDTO getEmployeeSeparation() {
        return employeeSeparation;
    }

    public void setEmployeeSeparation(EmployeeSeparationsDTO employeeSeparation) {
        this.employeeSeparation = employeeSeparation;
    }

    public Integer getDefaultNoticeDays() {
        return defaultNoticeDays;
    }

    public void setDefaultNoticeDays(Integer defaultNoticeDays) {
        this.defaultNoticeDays = defaultNoticeDays;
    }

    public Integer getMinimumNoticeDays() {
        return minimumNoticeDays;
    }

    public void setMinimumNoticeDays(Integer minimumNoticeDays) {
        this.minimumNoticeDays = minimumNoticeDays;
    }

    public Integer getGracePeriodDays() {
        return gracePeriodDays;
    }

    public void setGracePeriodDays(Integer gracePeriodDays) {
        this.gracePeriodDays = gracePeriodDays;
    }

    public Boolean getCanWaive() {
        return canWaive;
    }

    public void setCanWaive(Boolean canWaive) {
        this.canWaive = canWaive;
    }

    public Boolean getCanBuyout() {
        return canBuyout;
    }

    public void setCanBuyout(Boolean canBuyout) {
        this.canBuyout = canBuyout;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

}
