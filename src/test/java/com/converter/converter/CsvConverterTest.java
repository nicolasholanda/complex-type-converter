package com.converter.converter;

import com.converter.converter.csv.CsvString;
import com.converter.converter.csv.CsvToListConverter;
import com.converter.converter.csv.ListToCsvConverter;
import com.converter.exception.ConversionException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CsvConverterTest {

    private final CsvToListConverter toList = new CsvToListConverter();
    private final ListToCsvConverter toCsv = new ListToCsvConverter();

    @Test
    void parseCsvToList() {
        CsvString csv = new CsvString("name,age,city\nAlice,30,Springfield\nBob,25,Portland");
        List result = toList.convert(csv);
        assertThat(result).hasSize(2);
        assertThat(((Map<?, ?>) result.get(0)).get("name")).isEqualTo("Alice");
        assertThat(((Map<?, ?>) result.get(1)).get("city")).isEqualTo("Portland");
    }

    @Test
    void convertListToCsv() {
        List<Map<String, String>> rows = List.of(
                Map.of("name", "Alice", "age", "30"),
                Map.of("name", "Bob", "age", "25")
        );
        CsvString result = toCsv.convert(rows);
        assertThat(result.value()).contains("name").contains("Alice").contains("Bob");
    }

    @Test
    void roundTripCsvConversion() {
        CsvString original = new CsvString("product,price\nWidget,9.99\nGadget,24.99");
        List parsed = toList.convert(original);
        CsvString regenerated = toCsv.convert(parsed);
        assertThat(regenerated.value()).contains("Widget").contains("Gadget").contains("9.99");
    }

    @Test
    void headerOnlyThrows() {
        assertThatThrownBy(() -> toList.convert(new CsvString("name,age")))
                .isInstanceOf(ConversionException.class);
    }

    @Test
    void emptyListThrows() {
        assertThatThrownBy(() -> toCsv.convert(List.of()))
                .isInstanceOf(ConversionException.class);
    }

    @Test
    void sourceAndTargetTypesAreCorrect() {
        assertThat(toList.sourceType()).isEqualTo(CsvString.class);
        assertThat(toList.targetType()).isEqualTo(List.class);
        assertThat(toCsv.sourceType()).isEqualTo(List.class);
        assertThat(toCsv.targetType()).isEqualTo(CsvString.class);
    }
}
