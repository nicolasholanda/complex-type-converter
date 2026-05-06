package com.converter.core;

public record TypePair(Class<?> sourceType, Class<?> targetType) {

    @Override
    public String toString() {
        return sourceType.getSimpleName() + " -> " + targetType.getSimpleName();
    }
}
