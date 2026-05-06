package com.converter.config;

import com.converter.converter.csv.CsvToListConverter;
import com.converter.converter.csv.ListToCsvConverter;
import com.converter.converter.json.JsonToObjectConverter;
import com.converter.converter.json.ObjectToJsonConverter;
import com.converter.converter.temporal.LocalDateToStringConverter;
import com.converter.converter.temporal.StringToLocalDateConverter;
import com.converter.converter.temporal.StringToLocalDateTimeConverter;
import com.converter.converter.xml.ObjectToXmlConverter;
import com.converter.converter.xml.XmlToObjectConverter;
import com.converter.core.ConversionService;
import com.converter.core.ConverterRegistry;
import com.converter.impl.DefaultConversionService;
import com.converter.impl.DefaultConverterRegistry;
import com.converter.model.Address;
import com.converter.model.Order;
import com.converter.model.OrderItem;
import com.converter.model.Person;
import com.converter.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ConverterAutoConfiguration {

    @Bean
    public XmlMapper xmlMapper() {
        return XmlMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    @Bean
    public ConverterRegistry converterRegistry(ObjectMapper objectMapper, XmlMapper xmlMapper) {
        DefaultConverterRegistry registry = new DefaultConverterRegistry();

        List<Class<?>> domainTypes = List.of(
                Person.class, Address.class, Product.class, OrderItem.class, Order.class
        );

        for (Class<?> type : domainTypes) {
            registerJsonPair(registry, type, objectMapper);
            registerXmlPair(registry, type, xmlMapper);
        }

        registry.register(new CsvToListConverter());
        registry.register(new ListToCsvConverter());
        registry.register(new StringToLocalDateConverter());
        registry.register(new LocalDateToStringConverter());
        registry.register(new StringToLocalDateTimeConverter());

        return registry;
    }

    @Bean
    public ConversionService conversionService(ConverterRegistry registry) {
        return new DefaultConversionService(registry);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void registerJsonPair(DefaultConverterRegistry registry, Class<?> type, ObjectMapper mapper) {
        registry.register(new ObjectToJsonConverter(type, mapper));
        registry.register(new JsonToObjectConverter(type, mapper));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void registerXmlPair(DefaultConverterRegistry registry, Class<?> type, XmlMapper mapper) {
        registry.register(new ObjectToXmlConverter(type, mapper));
        registry.register(new XmlToObjectConverter(type, mapper));
    }
}
