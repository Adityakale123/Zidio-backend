# Stage 1: Build JAR using Maven
FROM maven:3.8-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run with Java
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/connect-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "-Dserver.port=10000", "app.jar"]