# Etapa 1: Compilar y empaquetar el JAR con Maven y Java 17
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos primero el pom.xml y el wrapper para aprovechar la caché de dependencias
COPY pom.xml mvnw mvnw.cmd ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copiamos el código fuente y generamos el .jar sin ejecutar tests bloqueantes
COPY src ./src
RUN ./mvnw clean package -DskipTests -B

# Etapa 2: Imagen ligera de ejecución solo con JRE 17
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copiamos el artefacto compilado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Render asigna dinámicamente el puerto mediante la variable PORT (por defecto 8080 en local)
EXPOSE 8080
ENV SERVER_PORT=8080

ENTRYPOINT ["sh", "-c", "java -Dserver.port=${PORT:-8080} -jar app.jar"]