package ru.leyman.manugen.templates.service;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.RequiredArgsConstructor;
import ru.leyman.manugen.templates.domain.entity.Field;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Converter
@RequiredArgsConstructor
public class FieldsAttributeConverter implements AttributeConverter<List<Field>, String> {

    private final ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(List<Field> fields) {
        return objectMapper.writeValueAsString(fields);
    }

    @Override
    public List<Field> convertToEntityAttribute(String s) {
        return objectMapper.readValue(s, List.class);
    }

}
