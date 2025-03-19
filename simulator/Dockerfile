# Stage di build: usa Maven per compilare l'applicazione
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /build

COPY pom.xml .
COPY ./simulation-platform ./simulation-platform
COPY ./drone-system ./drone-system

RUN mvn dependency:go-offline -B

RUN mvn clean package -DskipTests

# Stage di runtime: usa OpenJDK per eseguire l'applicazione
FROM openjdk:21-jdk-slim AS run

WORKDIR /app
COPY --from=build /build/simulation-platform/target/simulation-platform-0.0.1-SNAPSHOT.jar /app/simulation-platform-0.0.1-SNAPSHOT.jar

RUN apt-get update && apt-get install -y wget && apt-get clean && rm -rf /var/lib/apt/lists/*

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/simulation-platform-0.0.1-SNAPSHOT.jar"]