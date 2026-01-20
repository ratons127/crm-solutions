package com.betopia.hrm.domain.dto.attendance;


import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class DeviceTypeDTO {

    String workDate;
    String inTime;
    String outTime;
    String totalWorkDuration;

    public DeviceTypeDTO(String workDate, String inTime, String outTime, String totalWorkDuration) {
        this.workDate = workDate;
        this.inTime = inTime;
        this.outTime = outTime;
        this.totalWorkDuration = totalWorkDuration;
    }

    public String getWorkDate() {
        return workDate;
    }

    public void setWorkDate(String workDate) {
        this.workDate = workDate;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public String getTotalWorkDuration() {
        return totalWorkDuration;
    }

    public void setTotalWorkDuration(String totalWorkDuration) {
        this.totalWorkDuration = totalWorkDuration;
    }
}
