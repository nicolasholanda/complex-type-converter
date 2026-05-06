package com.converter.converter.temporal;

import com.converter.core.Converter;
import com.converter.exception.ConversionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    public LocalDateTime convert(String source) {
        try {
            return LocalDateTime.parse(source.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ConversionException("Cannot parse '" + source + "' as LocalDateTime — expected format: yyyy-MM-ddTHH:mm:ss", e);
        }
    }

    @Override
    public Class<String> sourceType() {
        return String.class;
    }

    @Override
    public Class<LocalDateTime> targetType() {
        return LocalDateTime.class;
    }
}
