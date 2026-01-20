package com.betopia.hrm.domain.attendance.entity;

import com.betopia.hrm.domain.admin.entity.Location;
import com.betopia.hrm.domain.base.entity.Auditable;
import jakarta.persistence.*;

@Entity
@Table(name = "attendance_devices")
public class AttendanceDevice extends Auditable<Long, Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relationship with AttendanceDeviceCategory
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private AttendanceDeviceCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(name = "device_name", nullable = false, length = 100)
    private String deviceName;

    @Column(name = "device_type", length = 100)
    private String deviceType;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "mac_address", length = 50)
    private String macAddress;

    @Column(name = "firmware_version", length = 50)
    private String firmwareVersion;

    @Column(name = "device_count", columnDefinition = "INT DEFAULT 0")
    private Long deviceCount;

    @Column(nullable = false)
    private Boolean status = true;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AttendanceDeviceCategory getCategory() {
        return category;
    }

    public void setCategory(AttendanceDeviceCategory category) {
        this.category = category;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
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
