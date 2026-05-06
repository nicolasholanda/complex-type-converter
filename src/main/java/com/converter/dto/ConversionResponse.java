package com.converter.dto;

public class ConversionResponse {

    private String sourceType;
    private String targetType;
    private String result;
    private boolean success;
    private String errorMessage;

    public static ConversionResponse ok(String sourceType, String targetType, String result) {
        ConversionResponse r = new ConversionResponse();
        r.sourceType = sourceType;
        r.targetType = targetType;
        r.result = result;
        r.success = true;
        return r;
    }

    public static ConversionResponse error(String sourceType, String targetType, String errorMessage) {
        ConversionResponse r = new ConversionResponse();
        r.sourceType = sourceType;
        r.targetType = targetType;
        r.success = false;
        r.errorMessage = errorMessage;
        return r;
    }

    public String getSourceType() { return sourceType; }
    public String getTargetType() { return targetType; }
    public String getResult() { return result; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }
}
