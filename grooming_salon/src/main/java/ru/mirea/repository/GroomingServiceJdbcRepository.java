package ru.mirea.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.mirea.model.GroomingService;

// Реализация репозитория через JDBC.
// С помощью SQL-запросов она работает напрямую с таблицей услуг в базе данных.
public class GroomingServiceJdbcRepository implements GroomingServiceRepository {

    // Сохраняет новую услугу в БД.
    @Override
    public void save(GroomingService service) {
        String sql = "INSERT INTO grooming_services (title, duration_minutes, price, description) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, service.title());
            ps.setInt(2, service.durationMinutes());
            ps.setBigDecimal(3, service.price());
            ps.setString(4, service.description());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении услуги в БД: " + e.getMessage(), e);
        }
    }

    // Ищет одну услугу по уникальному ID.
    @Override
    public Optional<GroomingService> findById(Long id) {
        String sql = "SELECT id, title, duration_minutes, price, description FROM grooming_services WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRowToGroomingService(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске услуги по ID=" + id + ": " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    // Возвращает все услуги из таблицы в порядке возрастания ID.
    @Override
    public List<GroomingService> findAll() {
        List<GroomingService> services = new ArrayList<>();
        String sql = "SELECT id, title, duration_minutes, price, description FROM grooming_services ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                services.add(mapRowToGroomingService(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка услуг: " + e.getMessage(), e);
        }

        return services;
    }

    // Обновляет данные услуги в БД.
    @Override
    public boolean update(GroomingService service) {
        String sql = "UPDATE grooming_services SET title = ?, duration_minutes = ?, price = ?, description = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, service.title());
            ps.setInt(2, service.durationMinutes());
            ps.setBigDecimal(3, service.price());
            ps.setString(4, service.description());
            ps.setLong(5, service.id());

            int updatedRows = ps.executeUpdate();
            return updatedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении услуги с ID=" + service.id() + ": " + e.getMessage(), e);
        }
    }

    // Удаляет услугу из таблицы по ID.
    @Override
    public boolean deleteById(Long id) {
        String sql = "DELETE FROM grooming_services WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);

            int deletedRows = ps.executeUpdate();
            return deletedRows > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении услуги с ID=" + id + ": " + e.getMessage(), e);
        }
    }

    // Преобразует строку из результата SQL-запроса в объект GroomingService.
    private GroomingService mapRowToGroomingService(ResultSet rs) throws SQLException {
        return new GroomingService(
                rs.getLong("id"),
                rs.getString("title"),
                rs.getInt("duration_minutes"),
                rs.getBigDecimal("price"),
                rs.getString("description"));
    }
}