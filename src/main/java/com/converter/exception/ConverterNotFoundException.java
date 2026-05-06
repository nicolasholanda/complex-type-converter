package com.converter.exception;

public class ConverterNotFoundException extends ConversionException {

    public ConverterNotFoundException(Class<?> sourceType, Class<?> targetType) {
        super("No converter found for " + sourceType.getSimpleName() + " -> " + targetType.getSimpleName());
    }
}
