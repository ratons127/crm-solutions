package com.betopia.hrm.cdc_data_pipeline.domain.parser;

public interface ColumnParser<IN, OUT> {
    OUT parse(IN value);
}
