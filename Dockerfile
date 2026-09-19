# =========================
# Etapa 1 - Build
# =========================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app2

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests


# =========================
# Etapa 2 - Runtime
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app2

COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

# ADICIONADO: -Xms128m -Xmx350m para a JVM não estourar a RAM do servidor
ENTRYPOINT ["java", "-Xms128m", "-Xmx350m", "-jar", "app.jar"]