package com.converter.controller;

import com.converter.converter.csv.CsvString;
import com.converter.converter.json.JsonString;
import com.converter.converter.xml.XmlString;
import com.converter.core.ConversionService;
import com.converter.core.TypePair;
import com.converter.dto.ConversionRequest;
import com.converter.dto.ConversionResponse;
import com.converter.exception.ConversionException;
import com.converter.model.Address;
import com.converter.model.Order;
import com.converter.model.OrderItem;
import com.converter.model.Person;
import com.converter.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ConversionController {

    private static final Map<String, Class<?>> TYPE_REGISTRY = new LinkedHashMap<>();

    static {
        TYPE_REGISTRY.put("Person", Person.class);
        TYPE_REGISTRY.put("Address", Address.class);
        TYPE_REGISTRY.put("Product", Product.class);
        TYPE_REGISTRY.put("OrderItem", OrderItem.class);
        TYPE_REGISTRY.put("Order", Order.class);
        TYPE_REGISTRY.put("JsonString", JsonString.class);
        TYPE_REGISTRY.put("XmlString", XmlString.class);
        TYPE_REGISTRY.put("CsvString", CsvString.class);
        TYPE_REGISTRY.put("String", String.class);
        TYPE_REGISTRY.put("LocalDate", LocalDate.class);
        TYPE_REGISTRY.put("LocalDateTime", LocalDateTime.class);
        TYPE_REGISTRY.put("List", List.class);
    }

    private final ConversionService conversionService;
    private final ObjectMapper objectMapper;

    public ConversionController(ConversionService conversionService, ObjectMapper objectMapper) {
        this.conversionService = conversionService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/converters")
    public List<String> listConverters() {
        return conversionService.listConverters().stream()
                .map(TypePair::toString)
                .sorted()
                .collect(Collectors.toList());
    }

    @PostMapping("/convert")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public ResponseEntity<ConversionResponse> convert(@RequestBody @Valid ConversionRequest request) {
        Class sourceClass = resolveType(request.getSourceType());
        Class targetClass = resolveType(request.getTargetType());

        Object sourceValue = toSourceValue(request.getValue(), sourceClass);
        Object result = conversionService.convert(sourceValue, sourceClass, targetClass);
        String resultString = toResultString(result);

        return ResponseEntity.ok(
                ConversionResponse.ok(request.getSourceType(), request.getTargetType(), resultString)
        );
    }

    @GetMapping("/types")
    public List<String> listTypes() {
        return List.copyOf(TYPE_REGISTRY.keySet());
    }

    private Class<?> resolveType(String typeName) {
        Class<?> type = TYPE_REGISTRY.get(typeName);
        if (type == null) {
            throw new ConversionException("Unknown type: '" + typeName + "'");
        }
        return type;
    }

    @SuppressWarnings("unchecked")
    private Object toSourceValue(String value, Class<?> sourceClass) {
        if (sourceClass == String.class) return value;
        if (sourceClass == JsonString.class) return new JsonString(value);
        if (sourceClass == XmlString.class) return new XmlString(value);
        if (sourceClass == CsvString.class) return new CsvString(value);
        try {
            return objectMapper.readValue(value, sourceClass);
        } catch (JsonProcessingException e) {
            throw new ConversionException(
                    "Cannot parse input as " + sourceClass.getSimpleName() + ": " + e.getMessage(), e);
        }
    }

    private String toResultString(Object result) {
        if (result instanceof JsonString js) return js.value();
        if (result instanceof XmlString xs) return xs.value();
        if (result instanceof CsvString cs) return cs.value();
        if (result instanceof String s) return s;
        try {
            return objectMapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            return result.toString();
        }
    }
}
