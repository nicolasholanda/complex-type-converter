package com.converter.converter.temporal;

import com.converter.core.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateToStringConverter implements Converter<LocalDate, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    @Override
    public String convert(LocalDate source) {
        return source.format(FORMATTER);
    }

    @Override
    public Class<LocalDate> sourceType() {
        return LocalDate.class;
    }

    @Override
    public Class<String> targetType() {
        return String.class;
    }
}
