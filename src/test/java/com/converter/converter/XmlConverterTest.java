package com.converter.converter;

import com.converter.converter.xml.ObjectToXmlConverter;
import com.converter.converter.xml.XmlString;
import com.converter.converter.xml.XmlToObjectConverter;
import com.converter.exception.ConversionException;
import com.converter.model.Address;
import com.converter.model.Person;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class XmlConverterTest {

    private XmlMapper xmlMapper;
    private Person person;

    @BeforeEach
    void setUp() {
        xmlMapper = XmlMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
        person = new Person("Bob", 25, "bob@example.com",
                new Address("10 Elm St", "Portland", "OR", "97201", "US"));
    }

    @Test
    void serializePersonToXml() {
        ObjectToXmlConverter<Person> converter = new ObjectToXmlConverter<>(Person.class, xmlMapper);
        XmlString result = converter.convert(person);
        assertThat(result.value()).contains("Bob").contains("bob@example.com").contains("<Person>");
    }

    @Test
    void deserializeXmlToPerson() {
        ObjectToXmlConverter<Person> toXml = new ObjectToXmlConverter<>(Person.class, xmlMapper);
        XmlToObjectConverter<Person> fromXml = new XmlToObjectConverter<>(Person.class, xmlMapper);

        XmlString xml = toXml.convert(person);
        Person restored = fromXml.convert(xml);

        assertThat(restored.name()).isEqualTo("Bob");
        assertThat(restored.age()).isEqualTo(25);
        assertThat(restored.address().city()).isEqualTo("Portland");
    }

    @Test
    void sourceTypeAndTargetTypeAreCorrect() {
        ObjectToXmlConverter<Person> converter = new ObjectToXmlConverter<>(Person.class, xmlMapper);
        assertThat(converter.sourceType()).isEqualTo(Person.class);
        assertThat(converter.targetType()).isEqualTo(XmlString.class);
    }

    @Test
    void deserializeInvalidXmlThrows() {
        XmlToObjectConverter<Person> converter = new XmlToObjectConverter<>(Person.class, xmlMapper);
        assertThatThrownBy(() -> converter.convert(new XmlString("<not valid xml")))
                .isInstanceOf(ConversionException.class);
    }
}
