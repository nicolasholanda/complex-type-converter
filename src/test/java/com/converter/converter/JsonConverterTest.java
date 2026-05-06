package com.converter.converter;

import com.converter.converter.json.JsonString;
import com.converter.converter.json.JsonToObjectConverter;
import com.converter.converter.json.ObjectToJsonConverter;
import com.converter.exception.ConversionException;
import com.converter.model.Address;
import com.converter.model.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JsonConverterTest {

    private ObjectMapper objectMapper;
    private Person person;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        person = new Person("Alice", 30, "alice@example.com",
                new Address("123 Main St", "Springfield", "IL", "62701", "US"));
    }

    @Test
    void serializePersonToJson() {
        ObjectToJsonConverter<Person> converter = new ObjectToJsonConverter<>(Person.class, objectMapper);
        JsonString result = converter.convert(person);
        assertThat(result.value()).contains("Alice").contains("alice@example.com");
    }

    @Test
    void deserializeJsonToPerson() {
        ObjectToJsonConverter<Person> toJson = new ObjectToJsonConverter<>(Person.class, objectMapper);
        JsonToObjectConverter<Person> fromJson = new JsonToObjectConverter<>(Person.class, objectMapper);

        JsonString json = toJson.convert(person);
        Person restored = fromJson.convert(json);

        assertThat(restored.name()).isEqualTo("Alice");
        assertThat(restored.age()).isEqualTo(30);
        assertThat(restored.address().city()).isEqualTo("Springfield");
    }

    @Test
    void sourceTypeAndTargetTypeAreCorrect() {
        ObjectToJsonConverter<Person> converter = new ObjectToJsonConverter<>(Person.class, objectMapper);
        assertThat(converter.sourceType()).isEqualTo(Person.class);
        assertThat(converter.targetType()).isEqualTo(JsonString.class);
    }

    @Test
    void deserializeInvalidJsonThrows() {
        JsonToObjectConverter<Person> converter = new JsonToObjectConverter<>(Person.class, objectMapper);
        assertThatThrownBy(() -> converter.convert(new JsonString("not valid json")))
                .isInstanceOf(ConversionException.class);
    }
}
