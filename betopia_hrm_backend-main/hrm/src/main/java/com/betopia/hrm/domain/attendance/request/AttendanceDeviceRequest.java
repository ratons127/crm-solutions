package com.betopia.hrm.domain.attendance.request;

import jakarta.validation.constraints.NotNull;

public record AttendanceDeviceRequest(
        Long id,

        Long categoryId,

        Long locationId,

        @NotNull(message = "Device name is required")
        String deviceName,

        @NotNull(message = "Device type is required")
        String deviceType,

        @NotNull(message = "Serial number is required")
        String serialNumber,

        @NotNull(message = "Ip address is required")
        String ipAddress,

        @NotNull(message = "Mac address is required")
        String macAddress,

        String firmwareVersion,

        @NotNull(message = "Total device is required")
        Long deviceCount,

        Boolean status
) {
}
