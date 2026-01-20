package com.betopia.hrm.cdc_data_pipeline.consumers;

import com.betopia.hrm.cdc_data_pipeline.domain.models.Payload;
import com.betopia.hrm.cdc_data_pipeline.domain.models.Root;
import com.betopia.hrm.cdc_data_pipeline.request.Message;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

@Component
public class TestConsumer {

    @Value("${enable.hard.delete.all}")
    private boolean enableCdcHardDeleteAll;

    private static Logger LOG = LoggerFactory.getLogger(TestConsumer.class.getSimpleName());

    private final JdbcTemplate jdbcTemplate;

    private final DataSource dataSource;

    public TestConsumer(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
    }

    @KafkaListener(topics = {"${topic.schema.ddl}"}, concurrency = "1")
    public void listenerSchema(ConsumerRecord<String, String> record
            , Acknowledgment ack) {
        String message = record.value();
        //Retrieve the message content:
        if (message == null){
            LOG.info("Schema-Change Message received but was null.");
            return;
        }
        LOG.info("Schema-Change Message received.");
        /////////////////////////////////////////////////////////////////////
        try {
            //TODO:
            LOG.info("topic.schema.ddl: not implemented yet.");
            ack.acknowledge();
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        /////////////////////////////////////////////////////////////////////
    }

    @KafkaListener(topics = {"${topic.schema.deviceAttendance}"}, concurrency = "1")
    public void listenerDeviceAttendanceTable(ConsumerRecord<String, String> record
            , Acknowledgment ack) {
        String message = record.value();
        //Retrieve the message content:
        if (message == null){
            LOG.info( "TimeClocks-Change Message received but was null.");
            return;
        }
        LOG.info( "TimeClocks-Change Message received.");
        /////////////////////////////////////////////////////////////////////
        try {
            Root root = Message.unmarshal(Root.class, message);;
            Payload payload = root.getPayload();
            //
            if (payload.getBefore() == null){
                LOG.info("Payload");
                //Insert:
                insertDeviceAttendance(payload.getAfter());
            } else if (payload.getAfter() == null){
                //Delete:
                if(enableCdcHardDeleteAll){
                    LOG.info("Hard delete is enabled.");
                    hardDelete((Integer) payload.getBefore().get("id"));
                } else {
                    LOG.info("Hard delete is disabled. Performing soft delete instead.");
                }

            } else if( payload.getAfter() != null && payload.getBefore() != null){
                //Update:
                if(payload.getAfter().get("deleted_at") == null){
                    //Normal update:
                    updateDeviceAttendance(payload.getAfter());
                } else {
                    //Soft delete:
                    softDelete((Integer) payload.getAfter().get("id"));
                }
            }else {
                LOG.info("Something went wrong with the payload: " +
                        payload.getAfter().entrySet().stream()
                                .map(e -> e.getKey() + "=" + e.getValue())
                                .collect(Collectors.joining(", ", "{", "}")));
            }
            //If all okay then ack:
            ack.acknowledge();
        } catch (Exception e) {
            LOG.info(e.getMessage(), e);
        }
        /////////////////////////////////////////////////////////////////////
    }

    @KafkaListener(topics = {"${topic.schema.deviceAttendance}.DLT"}, concurrency = "1")
    public void listenerDeviceAttendanceTableDLT(ConsumerRecord<String, String> record, Acknowledgment ack) {
        //Retrieve the message content:
        String message = record.value();
        if (message == null){
            LOG.info("Passenger-Table.DLT Message received but was null.");
            return;
        }
        LOG.info("Passenger-Table.DLT Message received.");
        ////////////////////////////////////////////////////////////////
        ack.acknowledge();
        ////////////////////////////////////////////////////////////////
    }

    // 🔹 Insert
    public void insertDeviceAttendance(Map<String, Object> data) {
        String sql = "INSERT INTO time_clocks " +
                "(id, clock_in, clock_out, break_minutes, is_late, overtime_minutes, geo_hash, work_date, created_at, updated_at, deleted_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        jdbcTemplate.update(sql,
                data.get("id"),
                data.get("ins") == null ? null : new Timestamp((Long) data.get("ins")),
                data.get("out") == null ? null : new Timestamp((Long) data.get("out")),
                data.get("break_minutes"),
                ((Integer) data.get("is_late")) == 1,
                data.get("overtime_minutes"),
                data.get("geo_hash"),
                data.get("work_date") == null ? null : convertWorkDate((Integer) data.get("work_date")),
                data.get("created_at") == null ? null :
                        Timestamp.valueOf(data.get("created_at").toString().replace("T", " ").replace("Z", "")),
                data.get("updated_at") == null ? null :
                        Timestamp.valueOf(data.get("updated_at").toString().replace("T", " ").replace("Z", "")),
                data.get("deleted_at") == null ? null :
                        Timestamp.valueOf(data.get("deleted_at").toString().replace("T", " ").replace("Z", ""))
        );
    }

    // 🔹 Update
    public void updateDeviceAttendance(Map<String, Object> data) {
        String sql = "UPDATE time_clocks " +
                "SET clock_out = ?, break_minutes = ?, is_late = ?, overtime_minutes = ?, updated_at = NOW() " +
                "WHERE id = ?";

        jdbcTemplate.update(sql,
                // clock_out
                data.get("out") != null ? new Timestamp((Long) data.get("out")) : null,

                // break_minutes
                data.get("break_minutes") != null ? (Integer) data.get("break_minutes") : null,

                // is_late (convert int → boolean)
                data.get("is_late") != null ? ((Integer) data.get("is_late")) == 1 : null,

                // overtime_minutes
                data.get("overtime_minutes") != null ? (Integer) data.get("overtime_minutes") : null,

                // id (primary key)
                data.get("id")
        );
    }

    // 🔹 Delete (soft delete by setting deleted_at)
    public void softDelete(int id) {
        String sql = "UPDATE time_clocks " +
                "SET deleted_at = NOW(), updated_at = NOW() " +
                "WHERE id = ?";

        jdbcTemplate.update(sql, id);
    }

    // 🔹 Hard Delete (optional)
    public void hardDelete(int id) {
        String sql = "DELETE FROM time_clocks WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    private Date convertWorkDate(Integer daysSinceEpoch) {
        LocalDate localDate = LocalDate.ofEpochDay(daysSinceEpoch);
        return Date.valueOf(localDate); // java.sql.Date
    }
}
