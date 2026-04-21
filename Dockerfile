# ============================================================
# Dockerfile — resto-pizza-web
# Pattern : multi-stage build identique à l'API
# Java 25 LTS — Eclipse Temurin
# ============================================================

# ─────────────────────────────────────────────
# STAGE 1 : BUILD
# ─────────────────────────────────────────────
FROM eclipse-temurin:25-jdk-alpine AS build
RUN apk add --no-cache maven
WORKDIR /app

# Même stratégie de cache : pom.xml d'abord, src/ ensuite
COPY pom.xml .
COPY src/ src/

# Compilation sans tests (les tests sont dans le pipeline Jenkins)
RUN mvn package -DskipTests --no-transfer-progress

# ─────────────────────────────────────────────
# STAGE 2 : RUN
# ─────────────────────────────────────────────
FROM eclipse-temurin:25-jre-alpine AS run
# Utilisateur non-root pour la sécurité
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Copier le .jar depuis le stage build
COPY --from=build /app/target/resto-pizza-web-0.0.1-SNAPSHOT.jar app.jar

RUN chown appuser:appgroup app.jar

USER appuser

# Le Web tourne généralement sur 8081 pour éviter le conflit avec l'API
# Vérifie ton application.properties : server.port=8081
EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]