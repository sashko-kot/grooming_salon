package ru.mirea.ui;

import ru.mirea.service.GroomingServiceService;

import java.util.Scanner;

// Главное меню приложения.
// Здесь пользователь выбирает, в каком режиме он хочет работать: администратор, клиент, аналитика или экспорт.
public class MainMenu {
    private final GroomingServiceService service;
    private final AdminMenuUI adminMenu;
    private final ClientMenuUI clientMenu;
    private final AnalyticsMenuUI analyticsMenu;
    private final ExportMenuUI exportMenu;

    // В конструкторе создаются все подменю приложения.
    public MainMenu(GroomingServiceService service) {
        this.service = service;
        this.adminMenu = new AdminMenuUI(service);
        this.clientMenu = new ClientMenuUI(service);
        this.analyticsMenu = new AnalyticsMenuUI(service);
        this.exportMenu = new ExportMenuUI(service);
    }

    // Запускает бесконечный цикл меню программы.
    public void start() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n========================================");
                System.out.println("        ГРУМИНГ-САЛОН");
                System.out.println("========================================");
                System.out.println("1. Администратор");
                System.out.println("2. Клиент");
                System.out.println("3. Аналитика");
                System.out.println("4. Экспорт данных");
                System.out.println("0. Выход");
                System.out.print("Выберите режим: ");

                switch (scanner.nextLine().trim()) {
                    case "1" -> adminMenu.show(scanner);
                    case "2" -> clientMenu.show(scanner);
                    case "3" -> analyticsMenu.show(scanner);
                    case "4" -> exportMenu.show(scanner);
                    case "0" -> {
                        System.out.println("Работа программы завершена.");
                        return;
                    }
                    default -> System.out.println("Ошибка: неизвестная команда.");
                }
            }
        }
    }
}
