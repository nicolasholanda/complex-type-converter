package com.converter.converter.xml;

import com.converter.core.Converter;
import com.converter.exception.ConversionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class XmlToObjectConverter<T> implements Converter<String, T> {

    private final Class<T> type;
    private final XmlMapper xmlMapper;

    public XmlToObjectConverter(Class<T> type, XmlMapper xmlMapper) {
        this.type = type;
        this.xmlMapper = xmlMapper;
    }

    @Override
    public T convert(String source) {
        try {
            return xmlMapper.readValue(source, type);
        } catch (JsonProcessingException e) {
            throw new ConversionException("Failed to deserialize XML to " + type.getSimpleName(), e);
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
