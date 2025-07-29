SS# 🛠️ Sistema de Mantenimiento de Vehículos

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


<figure>
  <img src="https://github.com/user-attachments/assets/2b679174-3fa7-41de-8b9e-b2869ba3e57d" width="600" alt="Pantalla de bienvenida" />
  <figcaption><strong>Pantalla de Bienvenida:</strong> Vista inicial del sistema, con opciones para iniciar sesión o registrarse.</figcaption>
</figure>

<figure>
  <img src="https://github.com/user-attachments/assets/22caeed2-2b84-4931-9dc6-04f62c75cd10" width="600" alt="Iniciar sesión" />
  <figcaption><strong>Iniciar Sesión:</strong> Formulario para que los usuarios ingresen con su nombre y contraseña.</figcaption>
</figure>

<figure>
  <img src="https://github.com/user-attachments/assets/18fcf10-a1d8-4645-b904-b7d90d5f5417" width="600" alt="Registro de usuario" />
  <figcaption><strong>Registro de Usuario:</strong> Formulario para crear una nueva cuenta con email y contraseña.</figcaption>
</figure>

<figure>
  <img src="https://github.com/user-attachments/assets/9fef2ca67-f9a4-4b36-b988-e3cf15c75682" width="600" alt="Vista principal usuario" />
  <figcaption><strong>Panel de Usuario:</strong> Vista principal donde se listan los vehículos registrados y se permite agregar uno nuevo.</figcaption>
</figure>

<figure>
  <img src="https://github.com/user-attachments/assets/5ea8a8f2-45f1-4231-b654-9873427dfc5f" width="600" alt="Formulario de vehículo" />
  <figcaption><strong>Registrar Vehículo:</strong> Formulario completo para ingresar datos del vehículo, kilometraje y correo del cliente.</figcaption>
</figure>

<figure>
  <img src="https://github.com/user-attachments/assets/1c5fe878-84bd-4b80-bd37-24b0ea7e4e23" width="600" alt="Panel administrador" />
  <figcaption><strong>Panel del Administrador:</strong> Interfaz para gestionar usuarios desde una cuenta con permisos administrativos.</figcaption>
</figure>

<figure>
  <img src="https://github.com/user-attachments/assets/4a8342a3-0d4d-4bd3-88a2-66333edf9be7" width="600" alt="Vista de envío de mail" />
  <figcaption><strong>Mail Enviado:</strong> Confirmación visual de que el sistema envió el correo al cliente correctamente.</figcaption>
</figure>

<figure>
  <img src="https://github.com/user-attachments/assets/e140c6cd-1db5-4715-989b-f5ca3d7a574b" width="600" alt="Otra vista de confirmación de correo" />
  <figcaption><strong>Notificación por Correo:</strong> Vista adicional del mensaje enviado al cliente con resumen del servicio.</figcaption>
</figure>








