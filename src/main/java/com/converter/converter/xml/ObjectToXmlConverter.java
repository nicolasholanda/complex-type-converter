package com.converter.converter.xml;

import com.converter.core.Converter;
import com.converter.exception.ConversionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ObjectToXmlConverter<S> implements Converter<S, String> {

    private final Class<S> type;
    private final XmlMapper xmlMapper;

    public ObjectToXmlConverter(Class<S> type, XmlMapper xmlMapper) {
        this.type = type;
        this.xmlMapper = xmlMapper;
    }

    @Override
    public String convert(S source) {
        try {
            return xmlMapper.writer()
                    .withRootName(type.getSimpleName())
                    .writeValueAsString(source);
        } catch (JsonProcessingException e) {
            throw new ConversionException("Failed to serialize " + type.getSimpleName() + " to XML", e);
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
