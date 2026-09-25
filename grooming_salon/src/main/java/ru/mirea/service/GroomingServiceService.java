package grooming.service;

import grooming.model.GroomingService;
import grooming.repository.GroomingServiceJdbcRepository;
import java.util.List;

public class GroomingServiceService {
    private final GroomingServiceJdbcRepository repository;

    public GroomingServiceService(GroomingServiceJdbcRepository repository) {
        this.repository = repository;
    }

    public List<GroomingService> getAllServices() {
        return repository.findAll();
    }

    public void addService(GroomingService service) {
        if (service.price().doubleValue() < 0) {
            throw new IllegalArgumentException("Цена не может быть отрицательной");
        }
        if (service.durationMinutes() <= 0) {
            throw new IllegalArgumentException("Длительность должна быть больше 0");
        }
        repository.save(service);
    }

    public GroomingService getServiceById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Услуга с ID " + id + " не найдена"));
    }

    public void deleteService(Long id) {
        repository.deleteById(id);
    }
}