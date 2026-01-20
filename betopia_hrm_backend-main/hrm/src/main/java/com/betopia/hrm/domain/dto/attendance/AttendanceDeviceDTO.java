package com.betopia.hrm.domain.dto.attendance;

import com.betopia.hrm.domain.dto.admin.LocationDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.ALWAYS)
public class AttendanceDeviceDTO {

    private Long id;

    private AttendanceDeviceCategoryDTO category;

    private LocationDTO location;

    private String deviceName;

    private String deviceType;

    private String serialNumber;

    private String ipAddress;

    private String macAddress;

    private String firmwareVersion;

    private Long deviceCount;

    private Boolean status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttendanceDeviceCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(AttendanceDeviceCategoryDTO category) {
        this.category = category;
    }

    public LocationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationDTO location) {
        this.location = location;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    public Long getDeviceCount() {
        return deviceCount;
    }

    public void setDeviceCount(Long deviceCount) {
        this.deviceCount = deviceCount;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
