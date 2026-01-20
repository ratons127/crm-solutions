package com.betopia.hrm.domain.dto.attendance.mapper;

import com.betopia.hrm.domain.attendance.entity.AttendanceDevice;
import com.betopia.hrm.domain.attendance.entity.AttendanceDeviceAssign;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceAssignDTO;
import com.betopia.hrm.domain.dto.attendance.AttendanceDeviceDTO;
import com.betopia.hrm.domain.dto.employee.EmployeeDTO;
import com.betopia.hrm.domain.employee.entity.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AttendanceDeviceAssignMapper {

    @Mapping(source = "attendanceDevice", target = "attendanceDevice", qualifiedByName = "toAttendanceDeviceDTO")
    @Mapping(source = "employee", target = "employee", qualifiedByName = "toEmployeeDTO")
    AttendanceDeviceAssignDTO toDTO(AttendanceDeviceAssign attendanceDeviceAssign);

    List<AttendanceDeviceAssignDTO> toDTOList(List<AttendanceDeviceAssign> attendanceDeviceAssigns);

    @Named("toAttendanceDeviceDTO")
    default AttendanceDeviceDTO toAttendanceDeviceDTO(AttendanceDevice attendanceDevice) {

        if (attendanceDevice == null)
        {
            return null;
        }

        AttendanceDeviceDTO dto = new AttendanceDeviceDTO();
        dto.setId(attendanceDevice.getId());
        dto.setDeviceName(attendanceDevice.getDeviceName());
        dto.setDeviceType(attendanceDevice.getDeviceType());
        dto.setStatus(attendanceDevice.getStatus());

        return dto;
    }

    // Custom mapping methods
    @Named("toEmployeeDTO")
    default EmployeeDTO toEmployeeDTO(Employee employee) {
        if (employee == null) {
            return null;
        }

        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setPhone(employee.getPhone());
        dto.setGender(employee.getGender());
        dto.setPresentAddress(employee.getPresentAddress());

        return dto;
    }
}
