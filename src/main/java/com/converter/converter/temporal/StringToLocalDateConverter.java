package com.converter.converter.temporal;

import com.converter.core.Converter;
import com.converter.exception.ConversionException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public LocalDate convert(String source) {
        try {
            return LocalDate.parse(source.trim(), FORMATTER);
        } catch (DateTimeParseException e) {
            throw new ConversionException("Cannot parse '" + source + "' as LocalDate — expected format: yyyy-MM-dd", e);
        }
    }

    @Override
    public Class<String> sourceType() {
        return String.class;
    }

    @Override
    public Class<LocalDate> targetType() {
        return LocalDate.class;
    }
}
