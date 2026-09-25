# Груминг-салон

Данное приложение представляет собой систему управления услугами груминг-салона. Оно позволяет:

- просматривать список услуг;
- добавлять новые услуги;
- редактировать и удалять существующие;
- искать услуги по ID или ключевому слову;
- выполнять базовую аналитику по ценам и длительности;
- экспортировать данные в CSV.

## Что входит в проект

- модель данных `GroomingService`;
- репозиторий для работы с PostgreSQL;
- сервисный слой для проверки данных;
- консольные меню для клиента, администратора и аналитики;
- экспорт в CSV;
- README с инструкцией по запуску.

## Технологии

- Java 17
- Maven
- PostgreSQL
- JDBC Driver

## Установка PostgreSQL

1. Установите PostgreSQL на свой компьютер.
2. Запустите pgAdmin или psql.
3. Создайте базу данных `grooming_db`.
4. Убедитесь, что PostgreSQL запущен на порту `5432`.

Пример создания БД:

```sql
CREATE DATABASE grooming_db;
```

## Настройка подключения

В файле `src/main/java/ru/mirea/repository/DatabaseConnection.java` настроены параметры подключения:

```java
private static final String URL = "jdbc:postgresql://localhost:5432/grooming_db";
private static final String USER = "postgres";
private static final String PASSWORD = "Sch4911D04!";
```

Если ваша локальная база имеет другие логин/пароль, измените эти значения.

## Запуск проекта

1. Откройте проект в IntelliJ IDEA или VS Code.
2. Убедитесь, что Maven установлен и настроен.
3. Запустите приложение через `App.java`.
4. В консоли появится меню груминг-салона.

## Структура проекта

```text
src/
├── main/
│   ├── java/
│   │   └── ru/
│   │       └── mirea/
│   │           ├── App.java
│   │           ├── analytics/
│   │           │   └── GroomingAnalytics.java
│   │           ├── exporter/
│   │           │   └── GroomingExporter.java
│   │           ├── model/
│   │           │   ├── Appointment.java
│   │           │   └── GroomingService.java
│   │           ├── repository/
│   │           │   ├── DatabaseConnection.java
│   │           │   ├── GroomingServiceJdbcRepository.java
│   │           │   └── GroomingServiceRepository.java
│   │           ├── service/
│   │           │   └── GroomingServiceService.java
│   │           └── ui/
│   │               ├── AdminMenuUI.java
│   │               ├── AnalyticsMenuUI.java
│   │               ├── ClientMenuUI.java
│   │               ├── ExportMenuUI.java
│   │               ├── InputHelper.java
│   │               └── MainMenu.java
│   └── resources/
│       ├── data.sql
│       └── schema.sql
└── test/
    └── java/
        └── ru/
            └── mirea/
                └── AppTest.java
```

## Ответственность по блокам

- Разработчик 1: модели данных и сущности.
- Разработчик 2: репозиторий и работа с БД.
- Разработчик 3: сервисный слой и бизнес-логика.
- Разработчик 4: пользовательский интерфейс и меню.
- Разработчик 5: `pom.xml`, зависимости, JDBC и экспорт данных.

## Экспорт данных

Приложение поддерживает экспорт списка услуг в CSV-файл. Формат выглядит так:

```csv
id,title,duration_minutes,price,description
1,Комплекс для собак мелких пород,90,2500.00,Купание, сушка...
```

## Проверка работы

После запуска приложения можно проверить:

1. запуск приложения;
2. подключение к БД;
3. добавление услуги;
4. просмотр списка услуг;
5. изменение и удаление;
6. клиентское меню;
7. аналитические отчёты;
8. экспорт CSV;
9. обработку ошибок ввода.

## Важное замечание

Если PostgreSQL не запущен или база `grooming_db` отсутствует, приложение не сможет подключиться к БД. В этом случае надо сначала создать базу и запустить сервер PostgreSQL.
