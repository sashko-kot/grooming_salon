package ru.mirea.ui;

import ru.mirea.analytics.GroomingAnalytics;
import ru.mirea.model.GroomingService;
import ru.mirea.service.GroomingServiceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AnalyticsMenuUI {
    private final GroomingServiceService service;
    private final GroomingAnalytics analytics = new GroomingAnalytics();

    public AnalyticsMenuUI(GroomingServiceService service) {
        this.service = service;
    }

    public void show(Scanner scanner) {
        while (true) {
            System.out.println("\n=== АНАЛИТИКА ===");
            System.out.println("1. Средняя стоимость");
            System.out.println("2. Медианная стоимость");
            System.out.println("3. Самая длинная процедура");
            System.out.println("4. Услуги не дороже заданной цены");
            System.out.println("5. Группировка: быстрые / комплексные");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> average();
                case "2" -> median();
                case "3" -> longest();
                case "4" -> cheaperThan(scanner);
                case "5" -> grouping();
                case "0" -> { return; }
                default -> System.out.println("Ошибка: неверный пункт меню.");
            }
        }
    }

    private List<GroomingService> services() {
        try {
            return service.getAllServices();
        } catch (RuntimeException e) {
            System.out.println("Ошибка при получении данных: " + e.getMessage());
            return List.of();
        }
    }

    private void average() {
        System.out.println("Средняя стоимость: " + analytics.calculateAveragePrice(services()) + " руб.");
    }

    private void median() {
        System.out.println("Медианная стоимость: " + analytics.calculateMedianPrice(services()) + " руб.");
    }

    private void longest() {
        GroomingService result = analytics.getLongestService(services());
        if (result == null) {
            System.out.println("Нет данных для анализа.");
            return;
        }
        System.out.printf("Самая длинная процедура: %s — %d мин.%n", result.title(), result.durationMinutes());
    }

    private void cheaperThan(Scanner scanner) {
        BigDecimal maxPrice = InputHelper.readMoney(scanner, "Максимальная цена: ");
        List<GroomingService> result = analytics.findServicesCheaperThan(services(), maxPrice);
        if (result.isEmpty()) {
            System.out.println("Подходящих услуг не найдено.");
            return;
        }
        result.forEach(s -> System.out.printf("%d. %s — %s руб.%n", s.id(), s.title(), s.price()));
    }

    private void grouping() {
        Map<String, List<GroomingService>> groups = analytics.groupByDuration(services());
        groups.forEach((name, list) -> {
            System.out.println("\n" + name + ":");
            list.forEach(s -> System.out.printf("- %s (%d мин.)%n", s.title(), s.durationMinutes()));
        });
    }
}
