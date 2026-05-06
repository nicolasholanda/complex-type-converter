package com.converter.handler;

import com.converter.dto.ConversionResponse;
import com.converter.exception.ConversionException;
import com.converter.exception.ConverterNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "com.converter.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(ConverterNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ConversionResponse handleNotFound(ConverterNotFoundException ex) {
        return ConversionResponse.error(null, null, ex.getMessage());
    }

    @ExceptionHandler(ConversionException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ConversionResponse handleConversionError(ConversionException ex) {
        return ConversionResponse.error(null, null, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ConversionResponse handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));
        return ConversionResponse.error(null, null, message);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ConversionResponse handleGeneric(Exception ex) {
        return ConversionResponse.error(null, null, "Unexpected error: " + ex.getMessage());
    }
}
