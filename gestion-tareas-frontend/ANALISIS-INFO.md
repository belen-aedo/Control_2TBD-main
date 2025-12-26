# Información del Módulo de Análisis

Este módulo implementa las consultas geoespaciales requeridas en el proyecto:

## Consultas Implementadas

### 1. **Promedio de Distancia** (`/api/tareas/analisis/promedio-distancia`)
**Pregunta:** ¿Cuál es el promedio de distancia de las tareas completadas respecto a la ubicación del usuario?
- **DTO:** `PromedioDistanciaDTO`
- **Campos:** 
  - `promedioDistanciaKm`: Promedio en kilómetros
  - `totalCompletadas`: Total de tareas completadas
- **Visualización:** Card destacada con el promedio en km

### 2. **Tarea Más Cercana** (`/api/tareas/analisis/mas-cercana`)
**Pregunta:** ¿Cuál es la tarea pendiente más cercana a la ubicación del usuario?
- **DTO:** `TareaCercanaDTO`
- **Campos:**
  - `idTarea`: ID de la tarea
  - `titulo`: Título de la tarea
  - `descripcion`: Descripción
  - `nombreSector`: Nombre del sector asociado
  - `distanciaKm`: Distancia en kilómetros
  - `fechaVencimiento`: Fecha de vencimiento
- **Visualización:** Card con información de la tarea más cercana pendiente

### 3. **Tareas por Sector** (`/api/tareas/analisis/por-sector`)
**Pregunta:** ¿Cuántas tareas ha hecho el usuario por sector?
- **DTO:** `TareasPorSectorDTO[]`
- **Campos:**
  - `nombreSector`: Nombre del sector
  - `cantidadTareas`: Cantidad de tareas en ese sector
- **Visualización:** Grid de cards por sector con barras de progreso

### 4. **Sector con Más Tareas en 2km** (`/api/tareas/analisis/sector-mas-tareas-2km`)
**Pregunta:** ¿Cuál es el sector con más tareas completadas en un radio de 2 kilómetros del usuario?
- **DTO:** `SectorConTareasDTO`
- **Campos:**
  - `nombreSector`: Nombre del sector
  - `cantidadTareasCompletadas`: Cantidad de tareas completadas
  - `distanciaKm`: Distancia al sector
- **Visualización:** Card horizontal destacada "Sector Más Activo (2 km)"

### 5. **Sector con Más Tareas en 5km** (`/api/tareas/analisis/sector-mas-tareas-5km`)
**Pregunta:** ¿Cuál es el sector con más tareas completadas dentro de un radio de 5 km desde la ubicación del usuario?
- **DTO:** `SectorConTareasDTO`
- **Campos:**
  - `nombreSector`: Nombre del sector
  - `cantidadTareasCompletadas`: Cantidad de tareas completadas
  - `distanciaKm`: Distancia al sector
- **Visualización:** Card horizontal destacada "Sector Más Activo (5 km)"

### 6. **Concentración de Tareas Pendientes** (`/api/tareas/analisis/concentracion-pendientes`)
**Pregunta:** ¿En qué sectores geográficos se concentran la mayoría de las tareas pendientes?
- **DTO:** `ConcentracionTareasDTO[]`
- **Campos:**
  - `nombreSector`: Nombre del sector
  - `cantidadTareasPendientes`: Cantidad de tareas pendientes
  - `latitud`: Latitud del sector
  - `longitud`: Longitud del sector
- **Visualización:** Lista de sectores con barras de concentración

## Tecnologías Utilizadas

- **Backend:** PostGIS para consultas geoespaciales
- **Frontend:** Vue 3 Composition API
- **Servicios:** Axios para llamadas HTTP

## Notas Importantes

- Todas las consultas requieren autenticación (token JWT)
- Las distancias se calculan usando funciones geoespaciales de PostGIS
- Los datos se obtienen en tiempo real al cargar la vista
- Si no hay datos, se muestran estados vacíos apropiados

