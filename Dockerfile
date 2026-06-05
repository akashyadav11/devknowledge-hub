
# ═══════════════════════════════════════════════════════
# Multi-stage build — keeps final image small
# Stage 1: build the jar with Maven
# Stage 2: run the jar with just JRE
# ═══════════════════════════════════════════════════════

# ── Stage 1: Build ────────────────────────────────────
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first — Docker caches dependencies
# so mvn install only re-runs when pom.xml changes
COPY pom.xml .
RUN mvn dependency:go-offline -q

# Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -q

# ── Stage 2: Run ──────────────────────────────────────
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy only the jar from build stage
COPY --from=build /app/target/*.jar app.jar

# Render assigns PORT via environment variable
EXPOSE 8081

# Keep heap low for Render free tier (512MB RAM)
ENTRYPOINT ["java", "-Xmx400m", "-Xms200m", "-jar", "app.jar"]