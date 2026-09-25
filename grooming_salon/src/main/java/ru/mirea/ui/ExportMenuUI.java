package ru.mirea.ui;

import ru.mirea.exporter.GroomingExporter;
import ru.mirea.service.GroomingServiceService;

import java.nio.file.Path;
import java.util.Scanner;

public class ExportMenuUI {
    private final GroomingServiceService service;

    public ExportMenuUI(GroomingServiceService service) {
        this.service = service;
    }

    public void show(Scanner scanner) {
        System.out.println("\n=== ЭКСПОРТ ===");
        String fileName = InputHelper.readRequiredString(scanner, "Имя CSV-файла [grooming_services.csv]: ");
        if (fileName.isBlank()) fileName = "grooming_services.csv";
        if (!fileName.toLowerCase().endsWith(".csv")) fileName += ".csv";
        try {
            Path target = Path.of(fileName);
            GroomingExporter.exportToCsv(service.getAllServices(), target);
            System.out.println("Экспорт выполнен: " + target.toAbsolutePath());
        } catch (Exception e) {
            System.out.println("Ошибка экспорта: " + e.getMessage());
        }
    }
}
