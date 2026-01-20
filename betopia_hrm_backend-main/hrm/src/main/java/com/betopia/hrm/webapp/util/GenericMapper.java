package com.betopia.hrm.webapp.util;

import com.betopia.hrm.domain.base.annotation.ExposeField;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class GenericMapper {

    public CommonDTO toDTO(Object entity) {
        CommonDTO dto = new CommonDTO();

        Field[] fields = entity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ExposeField.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(entity);
                    dto.addField(field.getName(), value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to map field: " + field.getName(), e);
                }
            }
        }

        return dto;
    }
}
