package com.converter.converter.csv;

import com.converter.core.Converter;
import com.converter.exception.ConversionException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CsvToListConverter implements Converter<CsvString, List> {

    @Override
    public List convert(CsvString source) {
        String[] lines = source.value().split("\n");
        if (lines.length < 2) {
            throw new ConversionException("CSV must have at least a header row and one data row");
        }

        String[] headers = parseLine(lines[0]);
        List<Map<String, String>> result = new ArrayList<>();

        for (int i = 1; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.isEmpty()) continue;
            String[] values = parseLine(line);
            Map<String, String> row = new LinkedHashMap<>();
            for (int j = 0; j < headers.length; j++) {
                row.put(headers[j].trim(), j < values.length ? values[j].trim() : "");
            }
            result.add(row);
        }

        return result;
    }

    private String[] parseLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    @Override
    public Class<CsvString> sourceType() {
        return CsvString.class;
    }

    @Override
    public Class<List> targetType() {
        return List.class;
    }
}
