package com.betopia.hrm.domain.attendance.entity;

import com.betopia.hrm.domain.base.entity.Auditable;
import jakarta.persistence.*;

@Entity
@Table(name = "attendance_device_category")
public class AttendanceDeviceCategory extends Auditable<Long, Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "communication_type", length = 50)
    private String communicationType;

    @Column(name = "biometric_mode", length = 50)
    private String biometricMode;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(nullable = false)
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

    public String getCommunicationType() {
        return communicationType;
    }

    public void setCommunicationType(String communicationType) {
        this.communicationType = communicationType;
    }

    public String getBiometricMode() {
        return biometricMode;
    }

    public void setBiometricMode(String biometricMode) {
        this.biometricMode = biometricMode;
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
}
