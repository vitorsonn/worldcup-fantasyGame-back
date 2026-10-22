# Etapa 1: Build da aplicacao (Maven + JDK 21)
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copia apenas o pom.xml primeiro para cache eficiente de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia o codigo fonte e gera o jar
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Runtime leve (apenas JRE 21)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Cria usuario nao-root por questoes de seguranca
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Copia o jar gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Expoe a porta padrao do Spring Boot
EXPOSE 8080

# Variaveis de memoria otimizadas para ambientes com recursos limitados (Render Free Tier)
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
