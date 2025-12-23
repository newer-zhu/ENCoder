# =========================
# Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /build

COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY ai-service/pom.xml ai-service/pom.xml

RUN mvn -B -q -DskipTests dependency:go-offline

COPY common common
COPY ai-service ai-service

RUN mvn -B -q -DskipTests clean package -pl ai-service -am


# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

ENV TZ=Asia/Shanghai \
    JAVA_OPTS="-Xms256m -Xmx512m"

COPY --from=builder /build/ai-service/target/*.jar app.jar

EXPOSE 8082

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
