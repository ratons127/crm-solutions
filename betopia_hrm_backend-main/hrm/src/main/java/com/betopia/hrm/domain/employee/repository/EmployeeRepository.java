package com.betopia.hrm.domain.employee.repository;

import com.betopia.hrm.domain.employee.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    @Query(value = "SELECT nextval('employee_serial_seq')", nativeQuery = true)
    long nextEmployeeSerial();

    @Query(value = "SELECT supervisor_id FROM employees WHERE id = :employeeId", nativeQuery = true)
    Optional<Employee> findSupervisorIdByEmployeeId(@Param("employeeId") Long employeeId);
    //boolean existsByTeam(Team team);
    Optional<Employee> findByEmployeeSerialId(Integer serialId);

}
