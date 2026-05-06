package com.converter.model;

import java.math.BigDecimal;

public record OrderItem(
        Product product,
        int quantity,
        BigDecimal unitPrice
) {}
