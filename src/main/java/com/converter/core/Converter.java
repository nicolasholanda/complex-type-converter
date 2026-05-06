package com.converter.core;

public interface Converter<S, T> {

    T convert(S source);

    Class<S> sourceType();

    Class<T> targetType();
}
