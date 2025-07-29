# 🛠️ Sistema de Mantenimiento de Vehículos

Aplicación web desarrollada con Spring Boot y Thymeleaf para gestionar el mantenimiento de vehículos en un lubricentro.

## 🚗 Funcionalidades

- Registro de vehículos con información completa del cliente.
- Cálculo automático del próximo servicio por fecha y kilometraje.
- Envío de recordatorios por correo electrónico.
- Edición y eliminación de vehículos existentes.

## 🛠️ Tecnologías

- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- Thymeleaf
- H2 / MySQL / SQLite (según configuración)
- JavaMail (enviar correos)
- Maven

## 📸 Capturas

📂 Estructura del proyecto

src/
 └─ 
 main/
 
     ├─ java/
     │   └─ com.lubricentro.mantenimiento/
     │       ├─ controller/
     │       ├─ model/
     │       ├─ repository/
     │       ├─ service/
     └─ resources/
         ├─ templates/
         └─ application.properties

👨‍💻 Autor
Juan Manuel
📧 juanma26@gmail.com



## ▶️ Cómo correr el proyecto

```bash
mvn spring-boot:run
