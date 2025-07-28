# Usa una imagen de Java
FROM eclipse-temurin:17-jdk

# Crea un directorio para la app
WORKDIR /app

# Copia el JAR construido
COPY target/lubricentro-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto (por defecto Spring Boot usa 8080)
EXPOSE 8080

# Comando para ejecutar la app
ENTRYPOINT ["java", "-jar", "app.jar"]
