# Build
FROM maven:3.8.5-openjdk-17-slim AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Runtime
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /app/target/backend-0.0.1.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]