package com.converter.impl;

import com.converter.converter.temporal.LocalDateToStringConverter;
import com.converter.converter.temporal.StringToLocalDateConverter;
import com.converter.core.ConversionService;
import com.converter.exception.ConversionException;
import com.converter.exception.ConverterNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class DefaultConversionServiceTest {

    private ConversionService service;

    @BeforeEach
    void setUp() {
        DefaultConverterRegistry registry = new DefaultConverterRegistry();
        registry.register(new StringToLocalDateConverter());
        registry.register(new LocalDateToStringConverter());
        service = new DefaultConversionService(registry);
    }

    @Test
    void convertWithInferredSourceType() {
        LocalDate result = service.convert("2024-06-15", LocalDate.class);
        assertThat(result).isEqualTo(LocalDate.of(2024, 6, 15));
    }

    @Test
    void convertWithExplicitSourceType() {
        LocalDate result = service.convert("2024-01-01", String.class, LocalDate.class);
        assertThat(result).isEqualTo(LocalDate.of(2024, 1, 1));
    }

    @Test
    void convertNullThrows() {
        assertThatThrownBy(() -> service.convert(null, LocalDate.class))
                .isInstanceOf(ConversionException.class)
                .hasMessageContaining("must not be null");
    }

    @Test
    void canConvertReturnsTrueWhenRegistered() {
        assertThat(service.canConvert(String.class, LocalDate.class)).isTrue();
        assertThat(service.canConvert(LocalDate.class, String.class)).isTrue();
    }

    @Test
    void canConvertReturnsFalseWhenNotRegistered() {
        assertThat(service.canConvert(Integer.class, LocalDate.class)).isFalse();
    }

    @Test
    void converterNotFoundThrows() {
        assertThatThrownBy(() -> service.convert(42, String.class))
                .isInstanceOf(ConverterNotFoundException.class);
    }

    @Test
    void listConvertersReturnsAllRegistered() {
        assertThat(service.listConverters()).hasSize(2);
    }
}
