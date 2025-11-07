# Используем официальный образ Gradle для сборки
FROM gradle:8.6-jdk21 AS builder

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем файлы для сборки
COPY build.gradle .
COPY settings.gradle .
COPY gradlew .
COPY gradle/ gradle/

# Копируем исходный код
COPY src/ src/

# Даем права на выполнение gradlew
RUN chmod +x gradlew

# Собираем приложение (пропускаем тесты для ускорения)
RUN ./gradlew build -x test

# Финальный образ
FROM eclipse-temurin:21-jre-alpine

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем собранный JAR из builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Создаем не-root пользователя для безопасности
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Открываем порт
EXPOSE 8080
EXPOSE 5432

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "/app/app.jar"]