# ---------- Estagio 1: BUILD (JDK + Maven) ----------
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn -B -q dependency:go-offline
COPY src ./src
RUN mvn -B -q package -DskipTests

# ---------- Estagio 2: RUNTIME (somente JRE enxuto) ----------
FROM eclipse-temurin:25-jre-alpine AS runtime
RUN addgroup -S app && adduser -S app -G app \
    && mkdir -p /app/data && chown -R app:app /app
WORKDIR /app
COPY --from=build /build/target/tarefas-api-1.0.0.jar app.jar
USER app
ENV DB_PATH=/app/data/tarefas
EXPOSE 8080
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
