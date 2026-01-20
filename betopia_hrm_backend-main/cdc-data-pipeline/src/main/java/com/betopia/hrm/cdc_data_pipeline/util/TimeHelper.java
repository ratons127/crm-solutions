package com.betopia.hrm.cdc_data_pipeline.util;

import java.time.LocalTime;

public class TimeHelper {

    public static LocalTime addMinutes(LocalTime time, int minutes, boolean maxSecond) {
        if (time == null) return null;
        LocalTime newTime = time.plusMinutes(minutes);
        return maxSecond ? newTime.withSecond(59) : newTime.withSecond(0);
    }

}
