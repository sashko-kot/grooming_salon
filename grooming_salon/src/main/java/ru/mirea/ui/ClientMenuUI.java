package ru.mirea.ui;

import ru.mirea.model.GroomingService;
import ru.mirea.service.GroomingServiceService;

import java.util.Scanner;

public class ClientMenuUI {
    private final GroomingServiceService service;

    public ClientMenuUI(GroomingServiceService service) {
        this.service = service;
    }

    public void show(Scanner scanner) {
        while (true) {
            System.out.println("\n=== МЕНЮ КЛИЕНТА ===");
            System.out.println("1. Посмотреть доступные услуги");
            System.out.println("2. Найти услугу по ID");
            System.out.println("0. Назад");
            System.out.print("Выберите действие: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> printServices();
                case "2" -> findService(scanner);
                case "0" -> { return; }
                default -> System.out.println("Ошибка: неверный пункт меню.");
            }
        }
    }

    private void printServices() {
        try {
            var services = service.getAllServices();
            if (services.isEmpty()) {
                System.out.println("Доступных услуг нет.");
                return;
            }
            System.out.printf("%-4s | %-35s | %-12s | %-10s%n", "ID", "Название", "Мин", "Цена");
            System.out.println("-".repeat(72));
            for (GroomingService s : services) {
                System.out.printf("%-4d | %-35s | %-12d | %-10s%n",
                        s.id(), s.title(), s.durationMinutes(), s.price());
            }
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void findService(Scanner scanner) {
        long id = InputHelper.readLong(scanner, "ID услуги: ", 1);
        try {
            service.getServiceById(id).ifPresentOrElse(
                    s -> {
                        System.out.println("\n" + s.title());
                        System.out.println("Длительность: " + s.durationMinutes() + " мин.");
                        System.out.println("Цена: " + s.price() + " руб.");
                        System.out.println("Описание: " + s.description());
                    },
                    () -> System.out.println("Услуга не найдена.")
            );
        } catch (RuntimeException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}
