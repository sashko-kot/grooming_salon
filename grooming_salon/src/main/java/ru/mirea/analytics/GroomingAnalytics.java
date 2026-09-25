package ru.mirea.analytics;

import ru.mirea.model.GroomingService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// Класс для анализа списка услуг: поиск, расчёты и группировка.
public class GroomingAnalytics {

    // Возвращает услуги, цена которых не превышает заданный максимум.
    public List<GroomingService> findServicesCheaperThan(List<GroomingService> services, BigDecimal maxPrice) {
        if (maxPrice == null) {
            throw new IllegalArgumentException("Максимальная цена не может быть пустой.");
        }

        return services.stream()
                .filter(s -> s.price() != null && s.price().compareTo(maxPrice) <= 0)
                .sorted(Comparator.comparing(GroomingService::price))
                .toList();
    }

    // Считает среднюю цену по всем услугам и округляет до 2 знаков после запятой.
    public BigDecimal calculateAveragePrice(List<GroomingService> services) {
        if (services == null || services.isEmpty()) {
            return BigDecimal.ZERO;
        }

        double avg = services.stream()
                .filter(s -> s.price() != null)
                .mapToDouble(s -> s.price().doubleValue())
                .average()
                .orElse(0.0);

        return BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
    }

    // Находит медиану цен: если количество чётное, берём среднее между двумя
    // серединами.
    public BigDecimal calculateMedianPrice(List<GroomingService> services) {
        if (services == null || services.isEmpty()) {
            return BigDecimal.ZERO;
        }

        List<BigDecimal> prices = services.stream()
                .map(GroomingService::price)
                .filter(price -> price != null)
                .sorted()
                .toList();

        if (prices.isEmpty()) {
            return BigDecimal.ZERO;
        }

        int middle = prices.size() / 2;
        if (prices.size() % 2 == 0) {
            BigDecimal left = prices.get(middle - 1);
            BigDecimal right = prices.get(middle);
            return left.add(right).divide(BigDecimal.valueOf(2), 2, RoundingMode.HALF_UP);
        }

        return prices.get(middle).setScale(2, RoundingMode.HALF_UP);
    }

    // Делит услуги на две группы: короткие и длинные по времени выполнения.
    public Map<String, List<GroomingService>> groupByDuration(List<GroomingService> services) {
        return services.stream()
                .collect(Collectors.groupingBy(
                        s -> s.durationMinutes() < 60 ? "Короткие (< 60 мин)" : "Долгие (>= 60 мин)"));
    }

    // Ищет услуги по ключевому слову в названии или описании.
    public List<GroomingService> searchByKeyword(List<GroomingService> services, String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return List.of();
        }

        String normalizedKeyword = keyword.trim().toLowerCase();
        return services.stream()
                .filter(s -> s.title() != null && s.title().toLowerCase().contains(normalizedKeyword)
                        || s.description() != null && s.description().toLowerCase().contains(normalizedKeyword))
                .toList();
    }

    // Возвращает самую длинную услугу по продолжительности.
    public GroomingService getLongestService(List<GroomingService> services) {
        if (services == null || services.isEmpty()) {
            return null;
        }

        return services.stream()
                .max(Comparator.comparingInt(GroomingService::durationMinutes))
                .orElse(null);
    }

    // Выводит список услуг, разбитый на категории по длительности.
    public void printServicesByDurationCategory(List<GroomingService> services) {
        System.out.println("--- Группировка по длительности ---");
        groupByDuration(services).forEach((category, list) -> {
            System.out.println(category + ":");
            list.forEach(s -> System.out.println("  - " + s.title() + " (" + s.durationMinutes() + " мин)"));
        });
    }
}