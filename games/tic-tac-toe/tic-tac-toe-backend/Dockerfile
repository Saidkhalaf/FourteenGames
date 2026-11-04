# Build stage
FROM gradle:8.10-jdk21 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle gradle
RUN gradle dependencies --no-daemon

COPY src ./src
RUN gradle clean build --no-daemon -x test

# Runtime stage
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
