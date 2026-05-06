package com.converter.converter.csv;

import com.converter.core.Converter;
import com.converter.exception.ConversionException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ListToCsvConverter implements Converter<List, CsvString> {

    @Override
    @SuppressWarnings("unchecked")
    public CsvString convert(List source) {
        if (source.isEmpty()) {
            throw new ConversionException("Cannot convert an empty list to CSV");
        }

        Object first = source.get(0);
        if (!(first instanceof Map)) {
            throw new ConversionException("List elements must be Map<String, String> instances");
        }

        Set<String> headers = new LinkedHashSet<>(((Map<String, String>) first).keySet());
        StringBuilder sb = new StringBuilder();
        sb.append(String.join(",", headers)).append("\n");

        for (Object item : source) {
            Map<String, String> row = (Map<String, String>) item;
            sb.append(headers.stream()
                    .map(h -> escape(row.getOrDefault(h, "")))
                    .reduce((a, b) -> a + "," + b)
                    .orElse(""))
              .append("\n");
        }

        return new CsvString(sb.toString().stripTrailing());
    }

    private String escape(String value) {
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }

    @Override
    public Class<List> sourceType() {
        return List.class;
    }

    @Override
    public Class<CsvString> targetType() {
        return CsvString.class;
    }
}
