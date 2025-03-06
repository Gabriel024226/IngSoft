FROM maven:3.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

# Copiar archivos de proyecto y descargar dependencias
COPY pom.xml .
COPY src ./src

# Empaquetar la aplicación
RUN mvn clean package -DskipTests

# Imagen final con solo JRE
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar el jar desde la etapa de build
COPY --from=build /app/target/HolaSpring-0.0.1-SNAPSHOT.jar app.jar

# Puerto a exponer
EXPOSE 8080

# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]