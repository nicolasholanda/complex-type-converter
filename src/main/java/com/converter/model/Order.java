package com.converter.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record Order(
        Long id,
        Person customer,
        List<OrderItem> items,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {}
