# ---- Etapa de Compilación ----
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar los archivos de Maven Wrapper y la configuración
mvnw.cmd pom.xml ./
COPY .mvn .mvn
COPY mvnw pom.xml ./

# Descargar dependencias (para aprovechar el caché de capas)
RUN ./mvnw dependency:go-offline

# Copiar el código fuente y empaquetar la aplicación
COPY src src
RUN ./mvnw package -DskipTests

# ---- Etapa de Ejecución ----
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copiar el JAR generado desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto predeterminado que usa Render (o la variable PORT)
EXPOSE 8080

# Comando para ejecutar la aplicac
ENTRYPOINT ["java", "-jar", "app.jar"]