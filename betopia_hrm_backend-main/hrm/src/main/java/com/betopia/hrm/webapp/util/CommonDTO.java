package com.betopia.hrm.webapp.util;

import java.util.HashMap;
import java.util.Map;

public class CommonDTO {

    private Map<String, Object> fields = new HashMap<>();

    public void addField(String key, Object value) {
        fields.put(key, value);
    }

    public Object getField(String key) {
        return fields.get(key);
    }

    public Map<String, Object> getFields() {
        return fields;
    }

    public Map<String, Object> toMap() {
        return fields;
    }
}
