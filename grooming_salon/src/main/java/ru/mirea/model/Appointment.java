package ru.mirea.model;

// Модель записи на приём.
// Здесь хранится информация о клиенте, питомце и детали записи.
public record Appointment(
        Long id,
        String clientName,
        String petName,
        Long serviceId,
        String bookingCode,
        String status) {
}
