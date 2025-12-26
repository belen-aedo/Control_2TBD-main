# 🚀 Sistema de Gestión de Tareas - Backend

API RESTful desarrollada con **Spring Boot** y **PostgreSQL/PostGIS** para la gestión geoespacial de tareas.

---

## 📋 Tabla de Contenidos

- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Endpoints de la API](#-endpoints-de-la-api)
- [Funcionalidades Implementadas](#-funcionalidades-implementadas)
- [Consultas Geoespaciales](#-consultas-geoespaciales)
- [Seguridad](#-seguridad)
- [Estado del Proyecto](#-estado-del-proyecto)

---

## 🛠 Tecnologías Utilizadas

- **Java 17**
- **Spring Boot 4.0.1**
- **Spring Security** (JWT Authentication)
- **Spring Data JPA**
- **PostgreSQL 15+**
- **PostGIS 3.x** (Extensión geoespacial)
- **Maven** (Gestión de dependencias)
- **Lombok** (Reducción de código boilerplate)

---

## 📦 Requisitos Previos

Antes de comenzar, asegúrate de tener instalado:

- **JDK 17** o superior
- **PostgreSQL 15+** con extensión **PostGIS**
- **Maven 3.8+**
- **Git**

---

## ⚙️ Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone <url-repositorio>
cd gestion-tareas-backend
```

### 2. Configurar PostgreSQL y PostGIS

Crea una base de datos en PostgreSQL:

```sql
CREATE DATABASE gestion_tareas_db;
\c gestion_tareas_db
CREATE EXTENSION postgis;
```

### 3. Configurar `application.properties`

Edita el archivo `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_tareas_db
spring.datasource.username=tu_usuario
spring.datasource.password=tu_contraseña
```

### 4. Ejecutar la aplicación

La aplicación ejecutará automáticamente el archivo `test.sql` que crea las tablas y datos de prueba.

```bash
mvn clean install
mvn spring-boot:run
```

El servidor estará disponible en: **http://localhost:8080**

### 5. Credenciales de prueba

Usuario predeterminado:
- **Usuario**: `admin`
- **Contraseña**: `1234`

---

## 📁 Estructura del Proyecto

```
src/main/java/cl/usach/tbd/gestiontareasbackend/
├── controller/
│   ├── AuthController.java          # Endpoints de autenticación
│   ├── TareaController.java         # Endpoints de tareas
│   └── SectorController.java        # Endpoints de sectores
├── dto/
│   ├── UsuarioRegistroRequest.java
│   ├── UsuarioLoginRequest.java
│   ├── LoginResponse.java
│   ├── TareaRequest.java
│   ├── TareaResponse.java
│   ├── SectorRequest.java
│   ├── SectorResponse.java
│   └── espacial/                     # DTOs para consultas geoespaciales
│       ├── PromedioDistanciaDTO.java
│       ├── TareaCercanaDTO.java
│       ├── TareasPorSectorDTO.java
│       ├── SectorConTareasDTO.java
│       └── ConcentracionTareasDTO.java
├── entity/
│   ├── Usuario.java                 # Entidad con ubicación geográfica (Point)
│   ├── Tarea.java                   # Entidad con relaciones
│   └── Sector.java                  # Entidad con ubicación espacial (Point)
├── repository/
│   ├── UsuarioRepository.java
│   ├── TareaRepository.java         # Consultas geoespaciales con PostGIS
│   └── SectorRepository.java
├── service/
│   ├── UsuarioService.java
│   ├── TareaService.java
│   └── SectorService.java
├── security/
│   ├── SecurityConfig.java          # Configuración de Spring Security
│   ├── JwtService.java              # Generación y validación de JWT
│   ├── JwtAuthenticationFilter.java # Filtro de autenticación
│   └── ApplicationConfig.java
└── GestionTareasApplication.java    # Clase principal
```

---

## 🌐 Endpoints de la API

### 🔐 Autenticación

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | `/api/auth/registro` | Registrar nuevo usuario con ubicación geográfica |
| POST | `/api/auth/login` | Iniciar sesión (retorna JWT) |

### 📍 Sectores

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/sectores` | Listar todos los sectores |
| POST | `/api/sectores` | Crear nuevo sector con ubicación espacial |

### 📝 Tareas

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/tareas` | Obtener todas las tareas del usuario autenticado |
| POST | `/api/tareas` | Crear nueva tarea |
| PUT | `/api/tareas/{id}` | Actualizar tarea |
| DELETE | `/api/tareas/{id}` | Eliminar tarea |
| PATCH | `/api/tareas/{id}/completar` | Marcar tarea como completada |
| GET | `/api/tareas/estado/{estado}` | Filtrar tareas por estado (PENDIENTE/COMPLETADA) |
| GET | `/api/tareas/buscar?keyword={texto}` | Buscar tareas por palabra clave |

### 📊 Análisis Geoespacial

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| GET | `/api/tareas/analisis/promedio-distancia` | Promedio de distancia de tareas completadas |
| GET | `/api/tareas/analisis/mas-cercana` | Tarea pendiente más cercana al usuario |
| GET | `/api/tareas/analisis/por-sector` | Cantidad de tareas por sector |
| GET | `/api/tareas/analisis/sector-mas-tareas-2km` | Sector con más tareas completadas en 2 km |
| GET | `/api/tareas/analisis/sector-mas-tareas-5km` | Sector con más tareas completadas en 5 km |
| GET | `/api/tareas/analisis/concentracion-pendientes` | Concentración de tareas pendientes por sector |

---

## ✅ Funcionalidades Implementadas

### Requisitos Funcionales

#### 1. ✅ Registro de Usuarios (100%)
- [x] Registro con nombre de usuario y contraseña
- [x] Almacenamiento de ubicación geográfica (Point con PostGIS)
- [x] Login con JWT
- [x] Contraseñas encriptadas con BCrypt

#### 2. ✅ Gestión de Tareas (100%)
- [x] Crear tareas con título, descripción, fecha y sector
- [x] Editar tareas existentes
- [x] Eliminar tareas
- [x] Marcar tareas como completadas
- [x] Ver lista de tareas (pendientes y completadas)

#### 3. ✅ Filtros y Búsqueda (100%)
- [x] Filtrar por estado (PENDIENTE/COMPLETADA)
- [x] Búsqueda por palabras clave en título y descripción

#### 4. ⚠️ Notificaciones (0%)
- [ ] Sistema de notificaciones por fecha de vencimiento
- **Nota**: Esta funcionalidad requiere implementación adicional (WebSockets, Email, etc.)

#### 5. ✅ Asociación con Sectores (100%)
- [x] Sectores georreferenciados con PostGIS
- [x] Asociación de tareas con sectores específicos
- [x] Almacenamiento de ubicación espacial

#### 6. ✅ Preguntas Geoespaciales (100%)
- [x] Tareas por sector por usuario
- [x] Tarea más cercana al usuario (pendiente)
- [x] Sector con más tareas completadas en 2 km
- [x] Promedio de distancia de tareas completadas
- [x] Concentración de tareas pendientes por sector
- [x] Sector con más tareas completadas en 5 km

---

## 🗺️ Consultas Geoespaciales

El sistema utiliza funciones de **PostGIS** para realizar cálculos espaciales:

### Funciones PostGIS Utilizadas

1. **ST_Distance**: Calcula distancias entre puntos geográficos
2. **ST_DWithin**: Filtra registros dentro de un radio específico
3. **ST_SetSRID**: Establece el sistema de referencia espacial (4326 - WGS84)
4. **ST_MakePoint**: Crea geometrías de tipo Point
5. **Geography Casting**: Conversión para cálculos precisos en metros

### Ejemplo de Consulta

```sql
-- Tarea más cercana al usuario
SELECT t.id_tarea, t.titulo, 
       ST_Distance(u.ubicacion_geografica::geography, 
                   s.ubicacion_espacial::geography) / 1000 as distanciaKm
FROM tarea t
INNER JOIN usuario u ON t.id_usuario = u.id_usuario
INNER JOIN sector s ON t.id_sector = s.id_sector
WHERE t.id_usuario = :idUsuario AND t.estado = 'PENDIENTE'
ORDER BY ST_Distance(u.ubicacion_geografica, s.ubicacion_espacial) ASC
LIMIT 1;
```

---

## 🔒 Seguridad

### Autenticación JWT

- **Algoritmo**: HS256
- **Expiración**: 24 horas
- **Header**: `Authorization: Bearer <token>`

### Protección Implementada

- [x] Encriptación de contraseñas con BCrypt
- [x] Autenticación basada en JWT
- [x] Autorización por usuario (solo acceso a sus propias tareas)
- [x] CORS configurado para el frontend
- [x] Protección contra inyección SQL (JPA/Hibernate)
- [ ] Protección CSRF (No necesaria para API REST stateless)

---

## 📊 Estado del Proyecto

### Progreso General: **85%**

| Categoría | Estado | Porcentaje |
|-----------|--------|------------|
| **Backend API RESTful** | ✅ Completo | 100% |
| **Base de Datos PostgreSQL/PostGIS** | ✅ Completo | 100% |
| **Autenticación y Autorización** | ✅ Completo | 100% |
| **Gestión de Tareas** | ✅ Completo | 100% |
| **Consultas Geoespaciales** | ✅ Completo | 100% |
| **Filtros y Búsqueda** | ✅ Completo | 100% |
| **Sistema de Notificaciones** | ❌ Pendiente | 0% |
| **Documentación** | ✅ Completo | 100% |
| **Testing** | ⚠️ Básico | 30% |
| **Despliegue Producción** | ⚠️ Pendiente | 0% |

### ✅ Completado

- ✅ API RESTful completamente funcional
- ✅ Integración con PostGIS
- ✅ Todas las consultas geoespaciales requeridas
- ✅ Autenticación JWT
- ✅ CRUD completo de tareas
- ✅ Gestión de sectores
- ✅ Filtros y búsqueda
- ✅ Seguridad básica implementada

### ⚠️ Pendiente

- ⚠️ Sistema de notificaciones automatizadas
- ⚠️ Tests unitarios e integración completos
- ⚠️ Documentación Swagger/OpenAPI
- ⚠️ Despliegue en producción
- ⚠️ Logs y monitoreo avanzado
- ⚠️ Validaciones más robustas

---

## 🚀 Próximos Pasos

1. **Notificaciones**: Implementar sistema de notificaciones por email o WebSocket
2. **Testing**: Aumentar cobertura de tests unitarios e integración
3. **Documentación API**: Integrar Swagger/OpenAPI
4. **Despliegue**: Configurar para producción (Docker, AWS, etc.)
5. **Optimización**: Revisar índices y queries para mejorar performance

---

## 👥 Autores

Proyecto desarrollado para el curso de Taller de Base de Datos - USACH

---

## 📄 Licencia

Este proyecto es de uso académico.

---

## 📞 Soporte

Para problemas o consultas, crear un issue en el repositorio.

