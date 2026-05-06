package com.converter.converter.json;

import com.converter.core.Converter;
import com.converter.exception.ConversionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectToJsonConverter<S> implements Converter<S, String> {

    private final Class<S> type;
    private final ObjectMapper objectMapper;

    public ObjectToJsonConverter(Class<S> type, ObjectMapper objectMapper) {
        this.type = type;
        this.objectMapper = objectMapper;
    }

    @Override
    public String convert(S source) {
        try {
            return objectMapper.writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new ConversionException("Failed to serialize " + type.getSimpleName() + " to JSON", e);
        }
    }

    @Override
    public Class<S> sourceType() {
        return type;
    }

    @Override
    public Class<String> targetType() {
        return String.class;
    }
}
