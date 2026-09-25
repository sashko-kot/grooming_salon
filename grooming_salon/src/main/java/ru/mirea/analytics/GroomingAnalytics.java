package grooming.analytics;

import grooming.model.GroomingService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

public class GroomingAnalytics {

    public List<GroomingService> findServicesCheaperThan(List<GroomingService> services, BigDecimal maxPrice) {
        return services.stream()
                .filter(s -> s.price().compareTo(maxPrice) <= 0)
                .sorted(Comparator.comparing(GroomingService::price))
                .toList();
    }

    public BigDecimal calculateAveragePrice(List<GroomingService> services) {
        if (services.isEmpty()) return BigDecimal.ZERO;

        double avg = services.stream()
                .mapToDouble(s -> s.price().doubleValue())
                .average()
                .orElse(0.0);

        return BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
    }

    public GroomingService getLongestService(List<GroomingService> services) {
        return services.stream()
                .max(Comparator.comparingInt(GroomingService::durationMinutes))
                .orElse(null);
    }

    public void printServicesByDurationCategory(List<GroomingService> services) {
        System.out.println("--- Группировка по длительности ---");
        services.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        s -> s.durationMinutes() < 60 ? "Короткие (< 60 мин)" : "Долгие (>= 60 мин)"
                ))
                .forEach((category, list) -> {
                    System.out.println(category + ":");
                    list.forEach(s -> System.out.println("  - " + s.title() + " (" + s.durationMinutes() + " мин)"));
                });
    }
}