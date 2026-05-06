package com.converter.model;

public record Address(
        String street,
        String city,
        String state,
        String zipCode,
        String country
) {}
