# Stage di build: usa Maven per compilare l'applicazione
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
LABEL maintainer="Volpato Mattia"

WORKDIR /build

COPY pom.xml . 
# Preleva tutte le dipendenze necessarie prima di copiare il codice sorgente
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

# Stage di runtime: usa OpenJDK per eseguire l'applicazione
FROM openjdk:21-jdk-slim AS run

WORKDIR /app
COPY --from=build /build/target/drone-identification-0.0.1-SNAPSHOT.jar /app/drone-identification-0.0.1-SNAPSHOT.jar

RUN apt-get update && apt-get install -y wget && apt-get clean && rm -rf /var/lib/apt/lists/*

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/drone-identification-0.0.1-SNAPSHOT.jar"]