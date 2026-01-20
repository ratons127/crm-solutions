package com.betopia.hrm.cdc_data_pipeline.domain.models;

public class Root {
    private Schema schema;
    private Payload payload;

    public Schema getSchema() {
        return schema;
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }
}
