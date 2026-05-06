package com.converter.impl;

import com.converter.core.Converter;
import com.converter.core.ConverterRegistry;
import com.converter.core.TypePair;
import com.converter.exception.ConverterNotFoundException;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultConverterRegistry implements ConverterRegistry {

    private final ConcurrentHashMap<TypePair, Converter<?, ?>> converters = new ConcurrentHashMap<>();

    @Override
    public <S, T> void register(Converter<S, T> converter) {
        converters.put(new TypePair(converter.sourceType(), converter.targetType()), converter);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S, T> Converter<S, T> find(Class<S> sourceType, Class<T> targetType) {
        Converter<?, ?> converter = converters.get(new TypePair(sourceType, targetType));
        if (converter == null) {
            throw new ConverterNotFoundException(sourceType, targetType);
        }
        return (Converter<S, T>) converter;
    }

    @Override
    public boolean supports(Class<?> sourceType, Class<?> targetType) {
        return converters.containsKey(new TypePair(sourceType, targetType));
    }

    @Override
    public Set<TypePair> listAll() {
        return Collections.unmodifiableSet(converters.keySet());
    }
}
