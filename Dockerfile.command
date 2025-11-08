# Build stage
FROM gradle:8.4-jdk17 AS build
WORKDIR /app

# Copy build files
COPY build.gradle settings.gradle ./
COPY shared-kernel ./shared-kernel
COPY command-service ./command-service

# Build
RUN gradle :command-service:bootJar --no-daemon

# Run stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/command-service/build/libs/*.jar app.jar

EXPOSE 8091

ENTRYPOINT ["java", "-jar", "app.jar"]
