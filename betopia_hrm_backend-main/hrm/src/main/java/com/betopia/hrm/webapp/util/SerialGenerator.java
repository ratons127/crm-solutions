package com.betopia.hrm.webapp.util;

import com.betopia.hrm.domain.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Component;

import java.time.Year;

@Component
public class SerialGenerator {
    private final long sequence=1;


    public static String generateSerial(String prefix, int year, long sequence) {
        return prefix + year + "-" + String.format("%04d", sequence);
    }
     //todo employee serial will implement dynamically
    public static String generateEmployeeSerial(long sequence) {
        int year = Year.now().getValue();
        return generateSerial("",year,sequence);
    }
}
