# ---------- Etapa 1: build ----------
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copia primeiro só o necessário pra resolver dependências (aproveita cache de camada do Docker)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Só agora copia o código-fonte e builda
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# ---------- Etapa 2: runtime ----------
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Usuário não-root, por segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]