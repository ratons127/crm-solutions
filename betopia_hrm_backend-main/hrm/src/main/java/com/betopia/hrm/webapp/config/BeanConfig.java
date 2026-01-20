package com.betopia.hrm.webapp.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Arrays;
import java.util.List;

@Configuration
public class BeanConfig {

    @Value("${calendar.generate.year}")
    private int calendarYear;

    @Value("${calendar.generate.types}")
    private String calendarTypes;

    @Value("${calendar.generate.force:false}")
    private boolean forceGenerate;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.region}")
    private String region;

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper jsonSerializer = new ObjectMapper();

        //Solution: Add Jackson JSR-310 Module. Jackson doesn't know how to (de)serialize java.time.LocalDateTime,
        // because Java 8 time types are not supported out-of-the-box unless you register the JSR-310 module.
        jsonSerializer.registerModule(new JavaTimeModule());
        jsonSerializer.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        jsonSerializer.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonSerializer.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        jsonSerializer.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        return jsonSerializer;
    }

    @Bean
    public CommandLineRunner initCalendar(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                System.out.println("🔄 Initializing calendar for year: " + calendarYear);

                // Check if calendar already exists
                Integer count = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM calendars WHERE year = ? AND type = ?",
                        Integer.class,
                        calendarYear, "HOLIDAY"
                );

                if (count != null && count > 0 && !forceGenerate) {
                    System.out.println("ℹ️ Calendar for year " + calendarYear + " already exists. Skipping generation.");

                    // Get existing calendar ID
                    Long existingId = jdbcTemplate.queryForObject(
                            "SELECT id FROM calendars WHERE year = ? AND type = ? LIMIT 1",
                            Long.class,
                            calendarYear, "HOLIDAY"
                    );
                    System.out.println("📅 Existing calendar ID: " + existingId);
                    return;
                }

                // Generate calendar using function
                String sql = "SELECT generate_calendar(?, ?, ?, ?)";

                Long calendarId = jdbcTemplate.queryForObject(
                        sql,
                        Long.class,
                        calendarYear,
                        "Company Calendar",
                        "HOLIDAY",
                        forceGenerate
                );

                if (calendarId != null) {
                    // Get statistics
                    Integer totalDays = jdbcTemplate.queryForObject(
                            "SELECT COUNT(*) FROM calendar_holidays WHERE calendar_id = ?",
                            Integer.class,
                            calendarId
                    );

                    Integer weekendDays = jdbcTemplate.queryForObject(
                            "SELECT COUNT(*) FROM calendar_holidays WHERE calendar_id = ? AND is_holiday = TRUE",
                            Integer.class,
                            calendarId
                    );

                    System.out.println("✅ Calendar generated successfully!");
                    System.out.println("📅 Calendar ID: " + calendarId);
                    System.out.println("📊 Total days: " + totalDays);
                    System.out.println("🏖️ Weekend days: " + weekendDays);
                    System.out.println("💼 Working days: " + (totalDays - weekendDays));
                } else {
                    System.err.println("❌ Failed to generate calendar - returned null");
                }

            } catch (DataAccessException e) {
                System.err.println("❌ Database error generating calendar: " + e.getMessage());
                e.printStackTrace();
                throw e;
            } catch (Exception e) {
                System.err.println("❌ Unexpected error generating calendar: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }


    @Bean
    public S3Client s3Client() {
        return S3Client.builder()
                .region(Region.of(region))
                .credentialsProvider(
                        StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey))
                )
                .build();
    }
}
