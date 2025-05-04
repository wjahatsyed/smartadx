# Stage 1: Build the application
FROM gradle:8.5-jdk21 AS builder

WORKDIR /app
COPY . .

# Build the Spring Boot fat jar
RUN gradle bootJar

# Stage 2: Run the application with lightweight JDK 21
FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Copy the jar from the build stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Expose application port
EXPOSE 8082

ENTRYPOINT ["java", "-jar", "app.jar"]
