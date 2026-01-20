package com.betopia.hrm.domain.dto.employee;

public class ShowAssignedEmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Long employeeSerialId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getEmployeeSerialId() {
        return employeeSerialId;
    }

    public void setEmployeeSerialId(Long employeeSerialId) {
        this.employeeSerialId = employeeSerialId;
    }
}
