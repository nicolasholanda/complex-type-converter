package com.converter.impl;

import com.converter.core.ConversionService;
import com.converter.core.Converter;
import com.converter.core.ConverterRegistry;
import com.converter.core.TypePair;
import com.converter.exception.ConversionException;

import java.util.Set;

public class DefaultConversionService implements ConversionService {

    private final ConverterRegistry registry;

    public DefaultConversionService(ConverterRegistry registry) {
        this.registry = registry;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <S, T> T convert(S source, Class<T> targetType) {
        if (source == null) {
            throw new ConversionException("Source value must not be null");
        }
        Class<S> sourceType = (Class<S>) source.getClass();
        return convert(source, sourceType, targetType);
    }

    @Override
    public <S, T> T convert(S source, Class<S> sourceType, Class<T> targetType) {
        if (source == null) {
            throw new ConversionException("Source value must not be null");
        }
        Converter<S, T> converter = registry.find(sourceType, targetType);
        try {
            return converter.convert(source);
        } catch (ConversionException e) {
            throw e;
        } catch (Exception e) {
            throw new ConversionException(
                "Conversion from " + sourceType.getSimpleName() + " to " + targetType.getSimpleName() + " failed: " + e.getMessage(), e
            );
        }
    }

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return registry.supports(sourceType, targetType);
    }

    @Override
    public Set<TypePair> listConverters() {
        return registry.listAll();
    }
}
