package com.betopia.hrm.cdc_data_pipeline.consumers;

import com.betopia.hrm.cdc_data_pipeline.domain.models.Payload;
import com.betopia.hrm.cdc_data_pipeline.domain.models.Root;
import com.betopia.hrm.cdc_data_pipeline.request.Message;
import com.betopia.hrm.cdc_data_pipeline.service.AttendanceService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.Map;

import static com.betopia.hrm.cdc_data_pipeline.util.DataUtils.convertToSqlTime;

@Component
public class AttendanceConsumer {

    @Value("${enable.hard.delete.all}")
    private boolean enableCdcHardDeleteAll;

    private static final Logger LOG = LoggerFactory.getLogger(AttendanceConsumer.class);

    private final AttendanceService attendanceService;

    public AttendanceConsumer(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    /**
     * Attendance CDC Listener
     **/
    @KafkaListener(topics = {"${topic.schema.iclock_transaction}"}, concurrency = "1")
    public void listenerAttendance(ConsumerRecord<String, String> record, Acknowledgment ack) {
        String message = record.value();
        if (message == null) {
            LOG.info("Attendance-Change Message received but was null.");
            return;
        }
        LOG.info("Attendance-Change Message received.");

        try {
            Root root = Message.unmarshal(Root.class, message);
            Payload payload = root.getPayload();

            Map<String, Object> after = payload.getAfter();
            Map<String, Object> before = payload.getBefore();

            Map<String, Object> data = after != null ? after : before;
            if (data == null) {
                LOG.warn("Both 'before' and 'after' are null — skipping message");
                return;
            }

            Map<String, Object> employee = attendanceService.getEmployeeByCode((String) data.get("emp_code"));
            if (employee == null) {
                LOG.warn("Employee not found for emp_code: {}", data.get("emp_code"));
                return;
            }

            processChange(before, after, employee);

            ack.acknowledge();

        } catch (Exception e) {
            LOG.error("Error processing attendance message: ", e);
        }
    }

    private void processChange(Map<String, Object> before, Map<String, Object> after, Map<String, Object> employee) {
        if (before == null && after != null) {
            handleInsertOrUpdate(after, employee);
        } else if (after == null && before != null) {
            handleDelete(before);
        } else if (after != null && before != null) {
            handleUpdate(after, employee);
        } else {
            LOG.warn("Unexpected CDC payload structure — both before and after are null.");
        }
    }

    private void handleInsertOrUpdate(Map<String, Object> after, Map<String, Object> employee) {
        LOG.info("Processing INSERT/UPDATE event for emp_code={}, punch_time={}",
                after.get("emp_code"), after.get("punch_time"));
        saveOrUpdateAttendance(after, employee);


    }

    private void handleDelete(Map<String, Object> before) {
        Integer id = (Integer) before.get("id");
        if (enableCdcHardDeleteAll) {
            LOG.info("Hard delete enabled — deleting id={}", id);
            attendanceService.hardDelete(id);
        } else {
            LOG.info("Soft delete — marking id={} as deleted", id);
            attendanceService.softDelete(id);
        }
    }

    private void handleUpdate(Map<String, Object> after, Map<String, Object> employee) {
        Integer id = (Integer) after.get("id");

        if (after.get("deleted_at") != null) {
            LOG.info("Soft delete (via update) detected for id={}", id);
            attendanceService.softDelete(id);
        } else {
            LOG.info("Updating attendance record id={}", id);
            saveOrUpdateAttendance(after, employee);

        }
    }

    public void saveOrUpdateAttendance(Map<String, Object> data, Map<String, Object> employee) {
        String empCode = (String) data.get("emp_code");
        Timestamp punchTime = convertToSqlTime(data.get("punch_time"));

        if (!attendanceService.attendanceExists(empCode, punchTime)) {
            LOG.info("Inserting new attendance for emp_code={}, punch_time={}", empCode, punchTime);
            attendanceService.insertAttendance(data);
            attendanceService.saveOrUpdateAttendanceSummary(data);
        } else {
            LOG.info("Updating punch_out_time for emp_code={}, punch_time={}", empCode, punchTime);
            attendanceService.updateAttendanceByPunchTime(
                    (String) data.get("emp_code"),
                    convertToSqlTime(data.get("punch_time")),
                    convertToSqlTime(data.get("upload_time"))
            );
            attendanceService.saveOrUpdateAttendanceSummary(
                    data
            );
        }
    }
}
