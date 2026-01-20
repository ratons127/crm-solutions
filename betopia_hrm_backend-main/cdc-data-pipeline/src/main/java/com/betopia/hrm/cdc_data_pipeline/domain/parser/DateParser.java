package com.betopia.hrm.cdc_data_pipeline.domain.parser;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateParser implements ColumnParser<Long, Date> {

    @Override
    public Date parse(Long value) {
        return convertSqlDate(value);
    }

    protected DateFormat getDateFormatter() {
        String pattern = "yyyy-MM-dd HH:mm:ss";
        return new SimpleDateFormat(pattern);
    }

    protected Date convertSqlDate(String datetime) throws ParseException {
        java.util.Date date = getDateFormatter().parse(datetime);
        return new Date(date.getTime());
    }

    protected Date convertSqlDate(long timestamp) {
        return new Date(timestamp);
    }
}
