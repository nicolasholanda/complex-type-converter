package com.converter.converter.json;

import com.converter.core.Converter;
import com.converter.exception.ConversionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonToObjectConverter<T> implements Converter<String, T> {

    private final Class<T> type;
    private final ObjectMapper objectMapper;

    public JsonToObjectConverter(Class<T> type, ObjectMapper objectMapper) {
        this.type = type;
        this.objectMapper = objectMapper;
    }

    @Override
    public T convert(String source) {
        try {
            return objectMapper.readValue(source, type);
        } catch (JsonProcessingException e) {
            throw new ConversionException("Failed to deserialize JSON to " + type.getSimpleName(), e);
        }
    }

    @Override
    public Class<String> sourceType() {
        return String.class;
    }

    @Override
    public Class<T> targetType() {
        return type;
    }
}
