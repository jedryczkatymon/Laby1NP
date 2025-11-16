# Budowanie aplikacji
FROM maven:3.9.11-amazoncorretto-24 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Ostateczny obraz
FROM eclipse-temurin:17-jre
WORKDIR /app

# Kopiowanie skompilowanej paczki JAR z etapu budowania
COPY --from=builder /app/target/*.jar /app/app.jar

# Kopiowanie pliku konfiguracyjnego
COPY src/main/resources/appsettings.docker.properties src/main/resources/appsettings.properties

# Uruchomienie aplikacji
ENTRYPOINT ["java", "-jar", "app.jar"]