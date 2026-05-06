package com.converter.core;

import java.util.Set;

public interface ConverterRegistry {

    <S, T> void register(Converter<S, T> converter);

    <S, T> Converter<S, T> find(Class<S> sourceType, Class<T> targetType);

    boolean supports(Class<?> sourceType, Class<?> targetType);

    Set<TypePair> listAll();
}
