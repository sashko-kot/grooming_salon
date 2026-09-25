package ru.mirea;

import ru.mirea.repository.DatabaseConnection;
import ru.mirea.repository.GroomingServiceJdbcRepository;
import ru.mirea.service.GroomingServiceService;
import ru.mirea.ui.MainMenu;

// Главный класс запуска приложения.
// Именно отсюда начинает работу вся программа.
public class App {
    public static void main(String[] args) {
        try {
            DatabaseConnection.initializeDatabase();

            GroomingServiceService service = new GroomingServiceService(new GroomingServiceJdbcRepository());
            MainMenu menu = new MainMenu(service);
            menu.start();
        } catch (Exception e) {
            System.out.println("Ошибка запуска приложения: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
