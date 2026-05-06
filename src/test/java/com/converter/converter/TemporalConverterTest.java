package com.converter.converter;

import com.converter.converter.temporal.LocalDateToStringConverter;
import com.converter.converter.temporal.StringToLocalDateConverter;
import com.converter.converter.temporal.StringToLocalDateTimeConverter;
import com.converter.exception.ConversionException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TemporalConverterTest {

    private final StringToLocalDateConverter toDate = new StringToLocalDateConverter();
    private final LocalDateToStringConverter dateToString = new LocalDateToStringConverter();
    private final StringToLocalDateTimeConverter toDateTime = new StringToLocalDateTimeConverter();

    @Test
    void parseIsoDateString() {
        LocalDate result = toDate.convert("2024-06-15");
        assertThat(result).isEqualTo(LocalDate.of(2024, 6, 15));
    }

    @Test
    void localDateToIsoString() {
        String result = dateToString.convert(LocalDate.of(2024, 6, 15));
        assertThat(result).isEqualTo("2024-06-15");
    }

    @Test
    void roundTripDateConversion() {
        String original = "2024-12-31";
        assertThat(dateToString.convert(toDate.convert(original))).isEqualTo(original);
    }

    @Test
    void parseIsoDateTimeString() {
        LocalDateTime result = toDateTime.convert("2024-06-15T10:30:00");
        assertThat(result).isEqualTo(LocalDateTime.of(2024, 6, 15, 10, 30, 0));
    }

    @Test
    void invalidDateStringThrows() {
        assertThatThrownBy(() -> toDate.convert("15/06/2024"))
                .isInstanceOf(ConversionException.class)
                .hasMessageContaining("yyyy-MM-dd");
    }

    @Test
    void invalidDateTimeStringThrows() {
        assertThatThrownBy(() -> toDateTime.convert("2024-06-15 10:30:00"))
                .isInstanceOf(ConversionException.class);
    }

    @Test
    void sourceAndTargetTypesAreCorrect() {
        assertThat(toDate.sourceType()).isEqualTo(String.class);
        assertThat(toDate.targetType()).isEqualTo(LocalDate.class);
        assertThat(dateToString.sourceType()).isEqualTo(LocalDate.class);
        assertThat(dateToString.targetType()).isEqualTo(String.class);
        assertThat(toDateTime.targetType()).isEqualTo(LocalDateTime.class);
    }
}
