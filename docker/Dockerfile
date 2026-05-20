FROM gradle:8.12-jdk21 AS build

WORKDIR /app

COPY gradle gradle
COPY gradlew .
COPY build.gradle .
COPY settings.gradle .
COPY common-events common-events
COPY order-service order-service
COPY storage-service storage-service

RUN chmod +x gradlew

ARG SERVICE_MODULE
RUN ./gradlew :${SERVICE_MODULE}:bootJar -x test

FROM eclipse-temurin:21-jre

WORKDIR /app

ARG SERVICE_MODULE
COPY --from=build /app/${SERVICE_MODULE}/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
