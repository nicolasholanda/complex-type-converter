package com.converter.converter.json;

import com.converter.core.Converter;
import com.converter.exception.ConversionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectToJsonConverter<S> implements Converter<S, JsonString> {

    private final Class<S> type;
    private final ObjectMapper objectMapper;

    public ObjectToJsonConverter(Class<S> type, ObjectMapper objectMapper) {
        this.type = type;
        this.objectMapper = objectMapper;
    }

    @Override
    public JsonString convert(S source) {
        try {
            return new JsonString(objectMapper.writeValueAsString(source));
        } catch (JsonProcessingException e) {
            throw new ConversionException("Failed to serialize " + type.getSimpleName() + " to JSON", e);
        }
    }

    @Override
    public Class<S> sourceType() {
        return type;
    }

    @Override
    public Class<JsonString> targetType() {
        return JsonString.class;
    }
}
