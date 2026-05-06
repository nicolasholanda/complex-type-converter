package com.converter.converter.csv;

public record CsvString(String value) {

    @Override
    public String toString() {
        return value;
    }
}
