package ru.mirea.model;

public record Appointment (
    Long id,
    String clientName,
    String petName,
    Long serviceId,
    String bookingCode,
    String status
) {}
