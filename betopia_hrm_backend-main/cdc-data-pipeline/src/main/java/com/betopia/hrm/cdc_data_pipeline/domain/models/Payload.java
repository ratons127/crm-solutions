package com.betopia.hrm.cdc_data_pipeline.domain.models;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class Payload {
    private int id;
    private String op;
    private Long ts_ms;
    private Source source;
    private Map<String, Object> before;
    private Map<String, Object> after;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public Long getTs_ms() {
        return ts_ms;
    }

    public void setTs_ms(Long ts_ms) {
        this.ts_ms = ts_ms;
    }

    public Map<String, Object> getBefore() {
        return before;
    }

    public void setBefore(Map<String, Object> before) {
        this.before = before;
    }

    public Map<String, Object> getAfter() {
        return after;
    }

    public void setAfter(Map<String, Object> after) {
        this.after = after;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    private final <T> T inflate(Map<String, Object> source, Class<T> type) {
        if (before == null) return null;
        try {
            //Class<?> clazz = Class.forName(type.getName());
            Constructor<?> any = type.getConstructor();
            T obj = (T) any.newInstance();
            //obj.unmarshallingFromMap(source, true);
            return obj;
        } catch (NoSuchMethodException | InstantiationException
                | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T inflateBefore(Class<T> type) {
        return inflate(getBefore(), type);
    }

    public <T> T inflateAfter(Class<T> type) {
        return inflate(getAfter(), type);
    }

}
