# 🎂 Birthday Notifier API

API REST desarrollada con Spring Boot para la gestión de miembros y la automatización de notificaciones de cumpleaños mediante correo electrónico y WhatsApp.

La aplicación permite administrar miembros, consultar cumpleaños actuales y próximos, realizar búsquedas dinámicas con filtros, paginación y ordenamiento, además de enviar notificaciones automáticas mediante tareas programadas.

---

## 🚀 Funcionalidades

### 👥 Gestión de Miembros

- Crear miembros
- Consultar miembros por ID
- Actualizar información de miembros
- Eliminar miembros
- Identificación mediante UUID

### 🎉 Gestión de Cumpleaños

- Consultar cumpleaños del día
- Consultar próximos cumpleaños
- Consultar cumpleaños del mes actual

### 🔍 Búsqueda Dinámica

- Filtros dinámicos utilizando Spring Data JPA Specifications
- Paginación
- Ordenamiento
- Consultas optimizadas

### 📨 Notificaciones Automáticas

- Recordatorios de cumpleaños
- Notificaciones de próximos cumpleaños
- Felicitaciones automáticas
- Integración con Email (Brevo SMTP)
- Integración con WhatsApp (CallMeBot)

### 📖 Calidad y Documentación

- Validación de datos
- Manejo global de excepciones
- Documentación OpenAPI / Swagger
- Logging estructurado
- Pruebas unitarias

---

## 🛠 Tecnologías Utilizadas

| Tecnología | Versión |
|------------|----------|
| Java | 17 |
| Spring Boot | 3.5 |
| Spring Data JPA | Última |
| PostgreSQL | 16 |
| Spring Validation | Incluido |
| Spring Mail | Incluido |
| Swagger / OpenAPI | 2.8.8 |
| Docker & Docker Compose | Incluido |
| Lombok | Incluido |
| Maven | Build Tool |
| JUnit 5 | Testing |

---

## 🏗 Arquitectura

La aplicación sigue una arquitectura por capas para garantizar mantenibilidad, escalabilidad y separación de responsabilidades.

```text
Controller
    ↓
Service
    ↓
Repository
    ↓
PostgreSQL
```

---

## 🐳 Arquitectura de Infraestructura

```text
┌─────────────────────────────┐
│ Birthday Notifier API       │
│ Spring Boot 3.5             │
│ Java 17                     │
│ Puerto 8080                 │
└──────────────┬──────────────┘
               │
               ▼
┌─────────────────────────────┐
│ PostgreSQL 16               │
│ Puerto 5432                 │
└─────────────────────────────┘
```

Ambos servicios se comunican mediante una red Docker dedicada:

```text
birthday-api-network
```

---

## 📂 Estructura del Proyecto

```text
src/main/java
├── config
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
├── mapper
├── repository
│   └── specification
├── schelduled
├── service
│   ├── impl
│   └── interfaces
├── utils
└── resources
```

---

## 👤 Entidad Principal: Member

Representa a un miembro registrado dentro de la organización.

### Campos

| Campo | Tipo |
|---------|---------|
| id | UUID |
| firstName | String |
| lastName | String |
| email | String |
| affiliationNumber | String |
| phoneNumber | String |
| birthDate | LocalDate |
| gender | Enum |
| memberRole | Enum |
| affiliationDate | LocalDate |
| active | Boolean |

### Reglas de Negocio

La entidad incorpora:

- Cálculo automático de edad
- Generación de nombre completo
- Cálculo de años de afiliación
- Normalización automática de datos
- Índices para optimizar búsquedas

---

## 📡 Endpoints

### Miembros

| Método | Endpoint | Descripción |
|----------|----------|----------|
| POST | `/api/v1/members` | Crear un nuevo miembro |
| GET | `/api/v1/members/{id}` | Obtener miembro por ID |
| GET | `/api/v1/members` | Buscar miembros con filtros y paginación |
| PATCH | `/api/v1/members/{id}` | Actualizar parcialmente un miembro |
| DELETE | `/api/v1/members/{id}` | Eliminar un miembro |

### Cumpleaños

| Método | Endpoint | Descripción |
|----------|----------|----------|
| GET | `/api/v1/birthdays/today` | Obtener cumpleaños del día |
| GET | `/api/v1/birthdays/upcoming` | Obtener próximos cumpleaños |
| GET | `/api/v1/birthdays/this-month` | Obtener cumpleaños del mes actual |

---

## 🔍 Búsqueda Dinámica y Paginación

La API soporta búsquedas dinámicas mediante Spring Data JPA Specifications.

### Ejemplo

```http
GET /api/v1/members?page=0&size=10&sort=createdAt,desc
```

### Características

- Filtros dinámicos
- Paginación
- Ordenamiento
- Consultas eficientes

---

## ⏰ Notificaciones Automáticas

La aplicación ejecuta tareas programadas para automatizar el envío de notificaciones.

### 🎂 Cumpleaños del Día

Detecta los miembros que cumplen años hoy y envía notificaciones automáticas.

### 📅 Próximos Cumpleaños

Detecta cumpleaños próximos y envía recordatorios.

### 🎉 Felicitaciones de Cumpleaños

Envía mensajes de felicitación mediante los canales configurados.

La programación de estas tareas es configurable mediante propiedades externas.

```yaml
api:
  birthday:
    current-cron:
    upcomming-cron:
    congratulations-cron:
```

---

## 🐳 Docker

La aplicación está completamente contenerizada utilizando Docker Compose.

### Servicios Incluidos

| Servicio | Descripción |
|----------|-------------|
| PostgreSQL | Base de datos principal |
| Birthday Notifier API | Aplicación Spring Boot |

### Levantar Todo el Entorno

```bash
docker compose up --build -d
```

### Verificar Contenedores

```bash
docker ps
```

### Detener Servicios

```bash
docker compose down
```

### Ver Logs de la Aplicación

```bash
docker logs birthday-notifier-app
```

---

## 🏭 Perfiles de Entorno

La aplicación soporta múltiples entornos.

### Desarrollo

Utiliza configuración local para la base de datos y servicios externos.

### Producción

Activado mediante:

```bash
SPRING_PROFILES_ACTIVE=production
```

La configuración productiva utiliza variables de entorno para proteger información sensible.

---

## 🔐 Variables de Entorno

Crear un archivo `.env` en la raíz del proyecto.

### Ejemplo

```env
POSTGRES_HOST=postgres
POSTGRES_USER=dev
POSTGRES_PASSWORD=tu_password

BREVO_HOST=smtp-relay.brevo.com
BREVO_USERNAME=tu_usuario
BREVO_PASSWORD=tu_password
SENDER_EMAIL=tu_email

CALLMEBOT_API_KEY=tu_api_key
```

---

## ⚙️ Instalación

### 1. Clonar el repositorio

```bash
git clone https://github.com/maxiamikel19/birthdayNotifierApi.git
```

### 2. Ingresar al proyecto

```bash
cd birthdayNotifierApi
```

### 3. Crear archivo de variables de entorno

```bash
cp .env.example .env
```

### 4. Configurar las variables necesarias

Editar el archivo `.env` con tus credenciales.

### 5. Construir y ejecutar los contenedores

```bash
docker compose up --build -d
```

---

## 📖 Documentación de la API

Una vez iniciada la aplicación:

### Swagger UI

```text
http://localhost:8080/docs
```

### OpenAPI JSON

```text
http://localhost:8080/api-docs
```

---

## 🧪 Pruebas

Ejecutar todas las pruebas:

```bash
mvn test
```

El proyecto incluye pruebas unitarias para la lógica de negocio de la capa de servicios.

---

## 🔒 Buenas Prácticas de Seguridad

- Las credenciales sensibles se gestionan mediante variables de entorno.
- Separación de configuraciones mediante Spring Profiles.
- Autenticación SMTP para el envío de correos.
- Las API Keys no deben almacenarse en el repositorio.
- Los contenedores Docker se ejecutan en una red privada aislada.

---

## 🔮 Mejoras Futuras

- Autenticación JWT
- Control de acceso basado en roles
- Migraciones con Flyway
- Pruebas de integración
- Pipeline CI/CD
- Monitoreo y métricas
- Despliegue en AWS
- Soporte para Kubernetes

---

## 👨‍💻 Autor

**Amikel Maxi**

Desarrollador Backend especializado en Java, Spring Boot, APIs REST, PostgreSQL, Docker y buenas prácticas de ingeniería de software.

GitHub: https://github.com/maxiamikel19

---

## 📄 Licencia

Proyecto desarrollado con fines educativos, demostrativos y de portafolio profesional.