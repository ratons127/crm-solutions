package com.betopia.hrm.cdc_data_pipeline.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Map;

public class DataUtils {

    // Converts object to SQL Timestamp
    public static Timestamp convertToSqlTime(Object value) {
        if (value == null) {
            return Timestamp.from(Instant.now());
        }

        try {
            long micros;
            if (value instanceof Number) {
                micros = ((Number) value).longValue();
            } else {
                micros = Long.parseLong(value.toString().trim());
            }

            long seconds = micros / 1_000_000;
            long nanos = (micros % 1_000_000) * 1_000;

            Instant instant = Instant.ofEpochSecond(seconds, nanos);

            String dateTimeStr = instant.toString().replace("Z", "");
            LocalDateTime ldt = LocalDateTime.parse(dateTimeStr);
            return Timestamp.valueOf(ldt);
        } catch (Exception e) {
            System.err.println("⚠️ Invalid punch_time format: " + value + " → defaulting to now()");
            return Timestamp.from(Instant.now());
        }
    }

    // Returns current timestamp or the provided value
    public static Timestamp timestampOrNow(Object value) {
        if (value == null) return new Timestamp(System.currentTimeMillis());
        String formatted = value.toString().replace("T", " ").replace("Z", "");
        return Timestamp.valueOf(formatted);
    }

    // Safely parses a double value from the given object
    public static Double safeDouble(Object value, String fieldName) {
        if (value == null) return null;
        try {
            String str = value.toString().trim();
            if (str.isEmpty() || !str.matches("[-+]?\\d*\\.?\\d+")) {
                return null;
            }
            return Double.parseDouble(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static Integer safeInt(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value instanceof Number ? ((Number) value).intValue() : null;
    }
}
