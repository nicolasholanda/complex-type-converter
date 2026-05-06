package com.converter.dto;

import jakarta.validation.constraints.NotBlank;

public class ConversionRequest {

    @NotBlank(message = "sourceType is required")
    private String sourceType;

    @NotBlank(message = "targetType is required")
    private String targetType;

    @NotBlank(message = "value is required")
    private String value;

    public ConversionRequest() {}

    public ConversionRequest(String sourceType, String targetType, String value) {
        this.sourceType = sourceType;
        this.targetType = targetType;
        this.value = value;
    }

    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
