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

COPY --from=builder /app2/target/*.jar app.jar

# ADICIONADO: Cria a pasta de dados do SQLite para evitar o erro CANTOPEN
RUN mkdir -p /app/data && chmod 777 /app/data

EXPOSE 8080

# ADICIONADO: flag de entropia e limites de memória
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Xms128m", "-Xmx350m", "-jar", "app.jar"]