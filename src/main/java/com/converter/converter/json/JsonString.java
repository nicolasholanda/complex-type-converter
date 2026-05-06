package com.converter.converter.json;

public record JsonString(String value) {

    @Override
    public String toString() {
        return value;
    }
}
