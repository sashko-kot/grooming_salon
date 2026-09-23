package ru.mirea.repository;

import java.util.List;
import java.util.Optional;

import ru.mirea.model.GroomingService;

public interface GroomingServiceRepository {
    void save(GroomingService service);
    Optional<GroomingService> findById(Long id);
    List<GroomingService> findAll();
    boolean update(GroomingService service);
    boolean deleteById(Long id);
}