package com.converter.controller;

import com.converter.converter.csv.CsvString;
import com.converter.converter.json.JsonString;
import com.converter.converter.xml.XmlString;
import com.converter.core.ConversionService;
import com.converter.dto.ConversionRequest;
import com.converter.exception.ConversionException;
import com.converter.model.Address;
import com.converter.model.Order;
import com.converter.model.OrderItem;
import com.converter.model.Person;
import com.converter.model.Product;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WebController {

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

    public WebController(ConversionService conversionService, ObjectMapper objectMapper) {
        this.conversionService = conversionService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("types", List.copyOf(TYPE_REGISTRY.keySet()));
        model.addAttribute("converters", conversionService.listConverters().stream()
                .map(Object::toString).sorted().toList());
        model.addAttribute("request", new ConversionRequest());
        return "index";
    }

    @PostMapping("/convert")
    @SuppressWarnings({"unchecked", "rawtypes"})
    public String convert(@ModelAttribute ConversionRequest request, Model model) {
        model.addAttribute("types", List.copyOf(TYPE_REGISTRY.keySet()));
        model.addAttribute("request", request);

        Class sourceClass = TYPE_REGISTRY.get(request.getSourceType());
        Class targetClass = TYPE_REGISTRY.get(request.getTargetType());

        if (sourceClass == null || targetClass == null) {
            model.addAttribute("error", "Unknown type specified.");
            return "index";
        }

        try {
            Object sourceValue = toSourceValue(request.getValue(), sourceClass);
            Object result = conversionService.convert(sourceValue, sourceClass, targetClass);
            model.addAttribute("result", toResultString(result));
            model.addAttribute("sourceType", request.getSourceType());
            model.addAttribute("targetType", request.getTargetType());
            model.addAttribute("inputValue", request.getValue());
            return "result";
        } catch (ConversionException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("converters", conversionService.listConverters().stream()
                    .map(Object::toString).sorted().toList());
            return "index";
        }
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
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
        } catch (JsonProcessingException e) {
            return result.toString();
        }
    }
}
