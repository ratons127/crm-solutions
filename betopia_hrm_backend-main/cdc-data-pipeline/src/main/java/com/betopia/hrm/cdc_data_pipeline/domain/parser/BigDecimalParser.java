package com.betopia.hrm.cdc_data_pipeline.domain.parser;

import java.math.BigDecimal;

public class BigDecimalParser implements ColumnParser<Double, BigDecimal> {
    @Override
    public BigDecimal parse(Double value) {
        BigDecimal decimal = new BigDecimal(value);
        return decimal;
    }
}
