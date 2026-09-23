package ru.mirea.model;

public record GroomingService (
    Long id,
    String title,
    int durationMinutes,
    java.math.BigDecimal price,
    String description
) {}
