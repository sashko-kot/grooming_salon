package ru.mirea.service;

import ru.mirea.model.GroomingService;
import ru.mirea.repository.GroomingServiceJdbcRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

// Бизнес-логика приложения.
// Этот класс проверяет данные и вызывает репозиторий для работы с услугами.
public class GroomingServiceService {
    private final GroomingServiceJdbcRepository repository;

    // В конструктор передаётся репозиторий для работы с БД.
    public GroomingServiceService(GroomingServiceJdbcRepository repository) {
        this.repository = repository;
    }

    // Возвращает список всех услуг.
    public List<GroomingService> getAllServices() {
        return repository.findAll();
    }

    // Добавляет готовый объект услуги после проверки корректности.
    public void addService(GroomingService service) {
        validateService(service);
        repository.save(service);
    }

    // Добавляет услугу по отдельным полям.
    public void addService(String title, int durationMinutes, BigDecimal price, String description) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название услуги не может быть пустым");
        }
        if (price == null) {
            throw new IllegalArgumentException("Цена не может быть пустой");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Длительность должна быть больше 0");
        }

        GroomingService service = new GroomingService(null, title.trim(), durationMinutes, price,
                description == null ? "" : description.trim());
        repository.save(service);
    }

    // Ищет услугу по её ID.
    public Optional<GroomingService> getServiceById(Long id) {
        return repository.findById(id);
    }

    // Обновляет существующую услугу.
    public boolean updateService(Long id, String title, int durationMinutes, BigDecimal price, String description) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID должен быть положительным");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Название услуги не может быть пустым");
        }
        if (price == null) {
            throw new IllegalArgumentException("Цена не может быть пустой");
        }
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Длительность должна быть больше 0");
        }

        GroomingService updated = new GroomingService(id, title.trim(), durationMinutes, price,
                description == null ? "" : description.trim());
        return repository.update(updated);
    }

    // Удаляет услугу по ID.
    public boolean deleteService(Long id) {
        return repository.deleteById(id);
    }

    // Внутренний метод проверки корректности объекта услуги.
    private void validateService(GroomingService service) {
        if (service == null) {
            throw new IllegalArgumentException("Услуга не может быть null");
        }
        if (service.title() == null || service.title().isBlank()) {
            throw new IllegalArgumentException("Название услуги не может быть пустым");
        }
        if (service.price() == null) {
            throw new IllegalArgumentException("Цена не может быть пустой");
        }
        if (service.price().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        if (service.durationMinutes() <= 0) {
            throw new IllegalArgumentException("Длительность должна быть больше 0");
        }
    }
}