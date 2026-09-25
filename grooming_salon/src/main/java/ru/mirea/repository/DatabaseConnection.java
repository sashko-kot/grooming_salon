package ru.mirea.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// Класс для подключения к базе данных PostgreSQL.
// Он хранит адрес сервера, логин и пароль,
// а затем создаёт соединение для работы с БД.
public final class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/grooming_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    private DatabaseConnection() {
    }

    // Метод создаёт и возвращает соединение с базой данных.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // Создаёт таблицы и наполняет базу тестовыми данными, если она пустая.
    public static void initializeDatabase() throws SQLException {
        try (Connection connection = getConnection();
                Statement statement = connection.createStatement()) {

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS grooming_services (
                        id BIGSERIAL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        duration_minutes INT NOT NULL CHECK (duration_minutes > 0),
                        price DECIMAL(10, 2) NOT NULL CHECK (price >= 0),
                        description VARCHAR(1000) NOT NULL
                    )
                    """);

            statement.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS appointments (
                        id BIGSERIAL PRIMARY KEY,
                        client_name VARCHAR(255) NOT NULL,
                        pet_name VARCHAR(255) NOT NULL,
                        service_id BIGINT NOT NULL REFERENCES grooming_services(id) ON DELETE CASCADE,
                        booking_code VARCHAR(50) NOT NULL UNIQUE,
                        status VARCHAR(50) NOT NULL DEFAULT 'NEW'
                    )
                    """);

            try (ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM grooming_services")) {
                if (resultSet.next() && resultSet.getInt(1) == 0) {
                    statement.executeUpdate(
                            """
                                    INSERT INTO grooming_services (title, duration_minutes, price, description) VALUES
                                    ('Комплекс для собак мелких пород', 90, 2500.00, 'Купание, сушка, гигиеническая стрижка, стрижка когтей, чистка ушей'),
                                    ('Комплекс для собак крупных пород', 120, 4200.00, 'Вычесывание, глубокая очистка шерсти, стрижка по стандарту породы, уход за когтями'),
                                    ('Экспресс-линька для кошек', 60, 2100.00, 'Удаление отмершего подшерстка специализированным инструментом, купание и сушка'),
                                    ('Гигиенический уход', 30, 1000.00, 'Подрезание когтей, обработка подушечек лап и гигиеническая чистка ушных раковин'),
                                    ('Стрижка кошек (без мытья)', 45, 1800.00, 'Модельная или гигиеническая стрижка машинкой без водных процедур')
                                    """);
                }
            }
        }
    }
}
