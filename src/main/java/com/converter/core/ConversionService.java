package com.converter.core;

import java.util.Set;

public interface ConversionService {

    <S, T> T convert(S source, Class<T> targetType);

    <S, T> T convert(S source, Class<S> sourceType, Class<T> targetType);

    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    Set<TypePair> listConverters();
}
