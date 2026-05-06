package com.converter.model;

public record Person(
        String name,
        int age,
        String email,
        Address address
) {}
