package ru.mirea.ui;

import ru.mirea.analytics.GroomingAnalytics;
import ru.mirea.model.GroomingService;
import ru.mirea.service.GroomingServiceService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

public class AdminMenuUI {
    private final GroomingServiceService service;
    private final GroomingAnalytics analytics;

    public AdminMenuUI(GroomingServiceService service) {
        this.service = service;
        this.analytics = new GroomingAnalytics(); 
    }

    public void show(Scanner scanner) {
        while (true) {
            System.out.println("\n=== ПАНЕЛЬ АДМИНИСТРАТОРА ===");
            System.out.println("1. Добавить услугу");
            System.out.println("2. Показать все услуги");
            System.out.println("3. Найти услугу по ID");
            System.out.println("4. Изменить услугу");
            System.out.println("5. Удалить услугу");
            System.out.println("6. Аналитика (средняя цена, самая долгая услуга, группировка)");
            System.out.println("7. Поиск услуг по ключевому слову");
            System.out.println("8. Показать услуги дешевле заданной цены");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> addService(scanner);
                case "2" -> printAll();
                case "3" -> findById(scanner);
                case "4" -> updateService(scanner);
                case "5" -> deleteService(scanner);
                case "6" -> showAnalytics();
                case "7" -> searchByKeyword(scanner);
                case "8" -> findCheaperThan(scanner);
                case "0" -> { return; }
                default -> System.out.println("Ошибка: неверный пункт меню.");
            }
        }
    }


    private void showAnalytics() {
        try {
            List<GroomingService> services = service.getAllServices();
            if (services.isEmpty()) {
                System.out.println("Нет данных для анализа.");
                return;
            }

            System.out.println("\n--- АНАЛИТИКА ---");
            System.out.println("Средняя стоимость услуги: " + analytics.calculateAveragePrice(services) + " руб.");

            GroomingService longest = analytics.getLongestService(services);
            if (longest != null) {
                System.out.println("Самая долгая услуга: " + longest.title()
                        + " (" + longest.durationMinutes() + " мин, " + longest.price() + " руб.)");
            }

            System.out.println("\n--- Группировка по длительности ---");
            Map<String, List<GroomingService>> grouped = analytics.groupByDuration(services);
            grouped.forEach((category, list) -> {
                System.out.println(category + ":");
                list.forEach(s -> System.out.println("  - " + s.title() + " (" + s.durationMinutes() + " мин)"));
            });

        } catch (RuntimeException e) {
            System.out.println("Ошибка аналитики: " + e.getMessage());
        }
    }

    private void searchByKeyword(Scanner scanner) {
        try {
            String keyword = InputHelper.readRequiredString(scanner, "Введите ключевое слово: ");
            List<GroomingService> found = analytics.searchByKeyword(service.getAllServices(), keyword);
            if (found.isEmpty()) {
                System.out.println("Ничего не найдено.");
            } else {
                System.out.println("Найдено услуг: " + found.size());
                printServices(found);
            }
        } catch (RuntimeException e) {
            System.out.println("Ошибка поиска: " + e.getMessage());
        }
    }

    private void findCheaperThan(Scanner scanner) {
        try {
            BigDecimal maxPrice = InputHelper.readMoney(scanner, "Максимальная цена (руб): ");
            List<GroomingService> cheap = analytics.findServicesCheaperThan(service.getAllServices(), maxPrice);
            if (cheap.isEmpty()) {
                System.out.println("Нет услуг дешевле " + maxPrice + " руб.");
            } else {
                System.out.println("Услуги дешевле " + maxPrice + " руб. (отсортированы по цене):");
                printServices(cheap);
            }
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }


    private void addService(Scanner scanner) {
        try {
            String title = InputHelper.readRequiredString(scanner, "Название услуги: ");
            int duration = InputHelper.readInt(scanner, "Длительность (мин): ", 1);
            BigDecimal price = InputHelper.readMoney(scanner, "Стоимость (руб): ");
            String description = InputHelper.readRequiredString(scanner, "Описание: ");
            service.addService(title, duration, price, description);
            System.out.println("Услуга успешно добавлена.");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void printAll() {
        try {
            List<GroomingService> services = service.getAllServices();
            printServices(services);
        } catch (RuntimeException e) {
            System.out.println("Ошибка при получении услуг: " + e.getMessage());
        }
    }

    private void findById(Scanner scanner) {
        long id = InputHelper.readLong(scanner, "Введите ID услуги: ", 1);
        try {
            Optional<GroomingService> result = service.getServiceById(id);
            result.ifPresentOrElse(
                    this::printService,
                    () -> System.out.println("Услуга с ID " + id + " не найдена.")
            );
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void updateService(Scanner scanner) {
        long id = InputHelper.readLong(scanner, "Введите ID услуги: ", 1);
        try {
            Optional<GroomingService> existing = service.getServiceById(id);
            if (existing.isEmpty()) {
                System.out.println("Услуга с таким ID не найдена.");
                return;
            }
            System.out.println("Текущие данные:");
            printService(existing.get());
            String title = InputHelper.readRequiredString(scanner, "Новое название: ");
            int duration = InputHelper.readInt(scanner, "Новая длительность (мин): ", 1);
            BigDecimal price = InputHelper.readMoney(scanner, "Новая стоимость (руб): ");
            String description = InputHelper.readRequiredString(scanner, "Новое описание: ");
            boolean updated = service.updateService(id, title, duration, price, description);
            System.out.println(updated ? "Услуга успешно изменена." : "Услуга не была изменена.");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void deleteService(Scanner scanner) {
        long id = InputHelper.readLong(scanner, "Введите ID услуги: ", 1);
        try {
            Optional<GroomingService> existing = service.getServiceById(id);
            if (existing.isEmpty()) {
                System.out.println("Услуга с таким ID не найдена.");
                return;
            }
            printService(existing.get());
            if (!InputHelper.readYesNo(scanner, "Удалить эту услугу?")) {
                System.out.println("Удаление отменено.");
                return;
            }
            System.out.println(service.deleteService(id) ? "Услуга удалена." : "Услуга не найдена.");
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void printServices(List<GroomingService> services) {
        if (services.isEmpty()) {
            System.out.println("Список услуг пуст.");
            return;
        }
        System.out.printf("%-4s | %-34s | %-12s | %-10s | %s%n", "ID", "Название", "Мин", "Цена", "Описание");
        System.out.println("-".repeat(110));
        for (GroomingService s : services) {
            System.out.printf("%-4d | %-34s | %-12d | %-10s | %s%n",
                    s.id(), cut(s.title(), 34), s.durationMinutes(), s.price(), cut(s.description(), 45));
        }
    }

    private void printService(GroomingService s) {
        System.out.println("ID: " + s.id());
        System.out.println("Название: " + s.title());
        System.out.println("Длительность: " + s.durationMinutes() + " мин.");
        System.out.println("Цена: " + s.price() + " руб.");
        System.out.println("Описание: " + s.description());
    }

    private String cut(String value, int max) {
        return value.length() <= max ? value : value.substring(0, max - 1) + "…";
    }
}
