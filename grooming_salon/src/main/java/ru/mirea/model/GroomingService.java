package ru.mirea.model;

// Модель услуги салона.
// В этом объекте хранится название, длительность, цена и описание услуги.
public record GroomingService(
        Long id,
        String title,
        int durationMinutes,
        java.math.BigDecimal price,
        String description) {
}
