package com.betopia.hrm.cdc_data_pipeline.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Message {

    protected <P> P unmarshalMessagePayload(Class<P> type, String payload) throws IOException {
        if (isValidJson(payload)) {
            ObjectMapper mapper = getJsonSerializer();
            P obj = mapper.readValue(payload, type);
            return obj;
        } else {
            return null;
        }
    }

    protected <P> String marshalMessagePayload(P object) throws IOException {
        if (object != null) {
            ObjectMapper mapper = getJsonSerializer();
            String value = mapper.writeValueAsString(object);
            return value;
        } else {
            return null;
        }
    }

    public String toString() {
        ObjectMapper mapper = getJsonSerializer();
        if (mapper != null) {
            try {
                String json = mapper.writeValueAsString(this);
                return json;
            } catch (JsonProcessingException var3) {
                var3.printStackTrace();
            }
        }

        return super.toString();
    }

    @JsonIgnore
    public static boolean isValidJson(String json) {
        if (json != null && !json.isEmpty()) {
            return json.trim().startsWith("{") || json.trim().startsWith("[");
        } else {
            return false;
        }
    }

    @JsonIgnore
    public static ObjectMapper getJsonSerializer() {
        ObjectMapper jsonSerializer = new ObjectMapper();
        jsonSerializer.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        jsonSerializer.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        jsonSerializer.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return jsonSerializer;
    }

    public static <P> P unmarshal(Class<P> type, String payload) throws IOException {
        return internalUnmarshal(type, payload);
    }

    public static <P> P unmarshal(TypeReference<P> type, String payload) throws IOException {
        return internalUnmarshal(type, payload);
    }

    private static <P> P internalUnmarshal(Object type, String payload) throws IOException {
        if (isValidJson(payload) && type != null) {
            ObjectMapper mapper = getJsonSerializer();
            Object obj;
            if (type instanceof TypeReference) {
                obj = mapper.readValue(payload, (TypeReference)type);
                return (P)obj;
            } else {
                obj = mapper.readValue(payload, (Class)type);
                return (P)obj;
            }
        } else {
            return null;
        }
    }

    public static <P> String marshal(P object) throws IOException {
        if (object != null) {
            ObjectMapper mapper = getJsonSerializer();
            String value = mapper.writeValueAsString(object);
            return value;
        } else {
            return null;
        }
    }
}
