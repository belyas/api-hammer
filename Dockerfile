# syntax=docker/dockerfile:1.7

FROM gradle:8.7-jdk21 AS build
WORKDIR /workspace

# Copy build scripts first to take advantage of Docker layer caching.
COPY settings.gradle build.gradle ./

# Copy the rest of the sources and build the Spring Boot fat jar.
COPY src src
RUN gradle clean bootJar -x test --no-daemon

FROM eclipse-temurin:21-jre-alpine AS runtime
RUN addgroup -S spring && adduser -S spring -G spring && apk add --no-cache curl
WORKDIR /app

COPY --from=build /workspace/build/libs/api-hammer.jar app.jar
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD curl --fail http://127.0.0.1:8080/health || exit 1

USER spring
ENTRYPOINT ["java", "-jar", "app.jar"]
