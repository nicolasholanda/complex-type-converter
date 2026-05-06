package com.converter.converter.xml;

public record XmlString(String value) {

    @Override
    public String toString() {
        return value;
    }
}
