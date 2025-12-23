# =========================
# Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-17 AS builder

WORKDIR /build

# 先拷贝 pom，利用缓存
COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY vocabulary-service/pom.xml vocabulary-service/pom.xml

RUN mvn -B -q -e -DskipTests dependency:go-offline

# 再拷贝源码
COPY common common
COPY vocabulary-service vocabulary-service

RUN mvn -B -q -DskipTests clean package -pl vocabulary-service -am


# =========================
# Runtime stage
# =========================
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

ENV TZ=Asia/Shanghai \
    JAVA_OPTS="-Xms256m -Xmx512m"

COPY --from=builder /build/vocabulary-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
