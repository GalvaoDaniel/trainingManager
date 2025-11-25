# Multi-stage Dockerfile for building and running the Spring Boot application
# Stage 1: build the application using the Gradle wrapper
FROM gradle:8.14.3-jdk17 AS builder
WORKDIR /home/gradle/project

# Copy only the files required for dependency resolution first (cache friendly)
COPY settings.gradle build.gradle gradlew ./
COPY gradle ./gradle

# Copy the source code
COPY src ./src

# Ensure the wrapper is executable and build the bootJar (skip tests for faster image builds)
RUN chmod +x gradlew \
    && ./gradlew clean bootJar -x test --no-daemon

# Stage 2: runtime image using a minimal JRE
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the jar produced by the builder stage
COPY --from=builder /home/gradle/project/build/libs/*.jar app.jar

# Expose application port (Spring Boot default)
EXPOSE 8080

# Allow passing JVM options via JAVA_OPTS environment variable
ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar /app/app.jar"]
