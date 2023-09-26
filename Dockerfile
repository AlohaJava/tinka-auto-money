# Используем официальный образ OpenJDK 17
FROM openjdk:17-slim AS build

# Устанавливаем рабочую директорию в контейнере
WORKDIR /app

# Копируем Gradle файлы проекта в контейнер
COPY .  /app/

# Собираем приложение
RUN ./gradlew bootJar

# Запускаем новый этап сборки для минимизации размера образа
FROM openjdk:17-slim

WORKDIR /app

# Копируем собранный JAR-файл из предыдущего этапа в текущий образ
COPY --from=build /app/build/libs/*.jar /app/myapp.jar

# Указываем порт, который будет использоваться приложением
EXPOSE 8080

# Запускаем наше приложение
CMD ["java", "-jar", "/app/myapp.jar"]