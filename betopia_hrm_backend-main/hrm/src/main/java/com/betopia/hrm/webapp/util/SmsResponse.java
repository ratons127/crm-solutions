package com.betopia.hrm.webapp.util;

public class SmsResponse {

    private String employeeId;
    private String mobileNumber;
    private String message;

    public SmsResponse(String employeeId, String mobileNumber, String message) {
        this.employeeId = employeeId;
        this.mobileNumber = mobileNumber;
        this.message = message;
    }

    public String getEmployeeId() { return employeeId; }
    public String getMobileNumber() { return mobileNumber; }
    public String getMessage() { return message; }

    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
    public void setMessage(String message) { this.message = message; }
}
