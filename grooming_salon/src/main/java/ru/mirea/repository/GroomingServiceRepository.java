package ru.mirea.repository;

import java.util.List;
import java.util.Optional;

import ru.mirea.model.GroomingService;

// Интерфейс репозитория услуг.
// Он задаёт обязательные операции для работы с данными: добавление, поиск, обновление и удаление.
public interface GroomingServiceRepository {
    // Сохраняет новую услугу.
    void save(GroomingService service);

    // Находит услугу по идентификатору.
    Optional<GroomingService> findById(Long id);

    // Возвращает все услуги из хранилища.
    List<GroomingService> findAll();

    // Обновляет уже существующую услугу.
    boolean update(GroomingService service);

    // Удаляет услугу по её ID.
    boolean deleteById(Long id);
}