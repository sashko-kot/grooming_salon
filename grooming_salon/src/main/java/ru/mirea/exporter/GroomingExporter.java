package ru.mirea.exporter;

import ru.mirea.model.GroomingService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class GroomingExporter {
    private GroomingExporter() {
    }

    public static void exportToCsv(List<GroomingService> services, Path target) throws IOException {
        if (services == null) {
            throw new IllegalArgumentException("Список услуг не может быть null.");
        }

        StringBuilder csv = new StringBuilder();
        csv.append("id,title,duration_minutes,price,description\n");

        for (GroomingService service : services) {
            csv.append(service.id())
                    .append(',')
                    .append(escape(service.title()))
                    .append(',')
                    .append(service.durationMinutes())
                    .append(',')
                    .append(service.price())
                    .append(',')
                    .append(escape(service.description()))
                    .append('\n');
        }

        Files.createDirectories(target.getParent() == null ? Path.of(".") : target.getParent());
        Files.writeString(target, csv.toString(), StandardCharsets.UTF_8);
    }

    private static String escape(String value) {
        if (value == null) {
            return "";
        }

        String normalized = value.replace("\"", "\"\"");
        if (normalized.contains(",") || normalized.contains("\n") || normalized.contains("\r")
                || normalized.contains("\"")) {
            return '"' + normalized + '"';
        }
        return normalized;
    }
}
