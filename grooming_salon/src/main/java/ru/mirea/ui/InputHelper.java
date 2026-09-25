package ru.mirea.ui;

import java.math.BigDecimal;
import java.util.Scanner;

// Утилитный класс для ввода данных с консоли.
// Он помогает безопасно считывать строки, числа, цену и ответы да/нет.
public final class InputHelper {
    private InputHelper() {
    }

    // Считывает непустую строку.
    public static String readRequiredString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Ошибка: значение не может быть пустым.");
        }
    }

    // Считывает целое число не меньше заданного минимума.
    public static int readInt(Scanner scanner, String prompt, int min) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= min) {
                    return value;
                }
                System.out.println("Ошибка: число должно быть не меньше " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
    }

    // Считывает длинное целое число не меньше заданного значения.
    public static long readLong(Scanner scanner, String prompt, long min) {
        while (true) {
            System.out.print(prompt);
            try {
                long value = Long.parseLong(scanner.nextLine().trim());
                if (value >= min) {
                    return value;
                }
                System.out.println("Ошибка: число должно быть не меньше " + min + ".");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
    }

    // Считывает цену и корректно обрабатывает запятую в числе.
    public static BigDecimal readMoney(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                BigDecimal value = new BigDecimal(scanner.nextLine().trim().replace(',', '.'));
                if (value.compareTo(BigDecimal.ZERO) >= 0) {
                    return value;
                }
                System.out.println("Ошибка: цена не может быть отрицательной.");
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число, например 2500.00.");
            }
        }
    }

    // Считывает ответ типа да/нет.
    public static boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt + " (д/н): ");
            String value = scanner.nextLine().trim().toLowerCase();
            if (value.equals("д") || value.equals("да") || value.equals("y") || value.equals("yes")) {
                return true;
            }
            if (value.equals("н") || value.equals("нет") || value.equals("n") || value.equals("no")) {
                return false;
            }
            System.out.println("Ошибка: введите д/н.");
        }
    }
}
