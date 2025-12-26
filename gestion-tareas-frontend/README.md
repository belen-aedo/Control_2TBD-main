# 🎨 Sistema de Gestión de Tareas - Frontend

Aplicación web moderna desarrollada con **Vue.js 3** para la gestión visual e interactiva de tareas geoespaciales.

---

## 📋 Tabla de Contenidos

- [Tecnologías Utilizadas](#-tecnologías-utilizadas)
- [Requisitos Previos](#-requisitos-previos)
- [Instalación y Configuración](#-instalación-y-configuración)
- [Estructura del Proyecto](#-estructura-del-proyecto)
- [Funcionalidades](#-funcionalidades)
- [Rutas de la Aplicación](#-rutas-de-la-aplicación)
- [Estado del Proyecto](#-estado-del-proyecto)
- [Capturas de Pantalla](#-capturas-de-pantalla)

---

## 🛠 Tecnologías Utilizadas

- **Vue.js 3.5** (Composition API)
- **Vue Router 4.6** (Navegación SPA)
- **Pinia 3.0** (Gestión de estado)
- **Axios 1.13** (Cliente HTTP)
- **Vite 7.2** (Build tool)
- **Leaflet 1.9** (Mapas interactivos)
- **CSS3** (Estilos modernos con variables)

---

## 📦 Requisitos Previos

- **Node.js 18+** y **npm 9+**
- Backend corriendo en `http://localhost:8080`

---

## ⚙️ Instalación y Configuración

### 1. Clonar el repositorio

```bash
git clone <url-repositorio>
cd gestion-tareas-frontend
```

### 2. Instalar dependencias

```bash
npm install
```

### 3. Configurar URL del Backend

Edita el archivo `src/core/api/axios.js` si tu backend no está en `localhost:8080`:

```javascript
const API_BASE_URL = 'http://localhost:8080';
```

### 4. Ejecutar en modo desarrollo

```bash
npm run dev
```

La aplicación estará disponible en: **http://localhost:5173**

### 5. Compilar para producción

```bash
npm run build
```

Los archivos compilados estarán en la carpeta `dist/`.

---

## 📁 Estructura del Proyecto

```
src/
├── main.js                           # Punto de entrada
├── App.vue                           # Componente raíz
├── style.css                         # Estilos globales
├── core/
│   ├── api/
│   │   └── axios.js                  # Configuración de Axios + Interceptores JWT
│   └── router/
│       └── index.js                  # Configuración de rutas
├── modules/
│   ├── auth/                         # Módulo de autenticación
│   │   ├── api/
│   │   │   └── auth.service.js       # Servicio de login/registro
│   │   └── views/
│   │       ├── LoginView.vue         # Pantalla de login
│   │       └── RegistroView.vue      # Pantalla de registro
│   ├── tareas/                       # Módulo de tareas
│   │   ├── api/
│   │   │   └── tareas.service.js     # Servicio de tareas
│   │   └── views/
│   │       ├── ListaTareas.vue       # Lista de tareas
│   │       └── FormularioTarea.vue   # Crear/editar tareas
│   ├── sectores/                     # Módulo de sectores
│   │   └── api/
│   │       └── sectores.service.js   # Servicio de sectores
│   ├── analisis/                     # Módulo de análisis geoespacial
│   │   ├── api/
│   │   │   └── analisis.service.js   # Servicio de análisis
│   │   └── views/
│   │       └── AnalisisView.vue      # Dashboard de análisis
│   └── core/
│       └── components/
│           └── Navbar.vue            # Barra de navegación
└── stores/
    └── auth.store.js                 # Store de Pinia para autenticación
```

---

## ✨ Funcionalidades

### 🔐 Autenticación

- **Login**: Inicio de sesión con JWT
- **Registro**: Creación de cuenta con ubicación geográfica (latitud/longitud)
- **Navegación protegida**: Rutas privadas con guards
- **Persistencia de sesión**: Token almacenado en localStorage

### 📝 Gestión de Tareas

- **Crear tareas**: Formulario con validaciones
  - Título (requerido, máx 100 caracteres)
  - Descripción (opcional, máx 500 caracteres)
  - Fecha de vencimiento (requerido, no puede ser pasada)
  - Sector (requerido, selección de sectores disponibles)
- **Listar tareas**: Vista en grid con tarjetas
  - Filtro visual por estado (PENDIENTE/COMPLETADA)
  - Información del sector asociado
  - Acciones rápidas (Completar/Eliminar)
- **Editar tareas**: Modificación de tareas existentes
- **Eliminar tareas**: Con confirmación
- **Marcar como completada**: Cambio de estado con un clic

### 🗺️ Sectores

- **Listar sectores**: Ver todos los sectores disponibles
- **Crear sectores**: Nuevo sector con ubicación geográfica

### 📊 Análisis Geoespacial

Panel de análisis con métricas visuales:

1. **Promedio de Distancia**
   - Distancia promedio entre tareas completadas y ubicación del usuario
   - Visualización con número grande y badge de total

2. **Tarea Más Cercana**
   - Muestra la tarea pendiente más próxima
   - Incluye distancia en km, sector y fecha de vencimiento
   - Estado vacío cuando no hay tareas pendientes

3. **Distribución por Sector**
   - Grid de tarjetas con iconos por sector
   - Cantidad de tareas por sector
   - Barra de progreso visual
   - Animación de entrada

4. **Sectores Más Activos**
   - Sector con más tareas en radio de 2 km
   - Sector con más tareas en radio de 5 km
   - Cantidad de tareas completadas

5. **Concentración de Tareas Pendientes**
   - Lista de sectores con más tareas por completar
   - Barra de progreso por sector
   - Ordenado de mayor a menor

### 🎨 Interfaz de Usuario

- **Diseño moderno**: Interfaz limpia y profesional
- **Dark theme**: Tema oscuro por defecto
- **Responsive**: Adaptable a móviles y tablets
- **Animaciones**: Transiciones suaves y feedback visual
- **Iconos**: Emojis modernos para mejor UX
- **Loading states**: Indicadores de carga
- **Confirmaciones**: Diálogos antes de acciones destructivas

---

## 🗺️ Rutas de la Aplicación

| Ruta | Componente | Protección | Descripción |
|------|------------|------------|-------------|
| `/` | Redirect → `/login` | Pública | Redirección inicial |
| `/login` | LoginView | Pública | Inicio de sesión |
| `/registro` | RegistroView | Pública | Registro de usuarios |
| `/tareas` | ListaTareas | Privada | Lista de tareas |
| `/tareas/nueva` | FormularioTarea | Privada | Crear nueva tarea |
| `/analisis` | AnalisisView | Privada | Dashboard de análisis |

---

## 📊 Estado del Proyecto

### Progreso General: **90%**

| Categoría | Estado | Porcentaje |
|-----------|--------|------------|
| **Interfaz de Usuario** | ✅ Completo | 100% |
| **Autenticación** | ✅ Completo | 100% |
| **Gestión de Tareas** | ✅ Completo | 100% |
| **Gestión de Sectores** | ✅ Completo | 100% |
| **Análisis Geoespacial** | ✅ Completo | 100% |
| **Filtros y Búsqueda** | ⚠️ Parcial | 70% |
| **Notificaciones** | ❌ Pendiente | 0% |
| **Mapas Interactivos** | ⚠️ Pendiente | 0% |
| **Responsive Design** | ✅ Completo | 100% |
| **Testing** | ❌ Pendiente | 0% |

---

## ✅ Completado

### Requisitos Funcionales

#### 1. ✅ Registro de Usuarios (100%)
- [x] Formulario de registro
- [x] Input para ubicación geográfica (lat/lng)
- [x] Validaciones de formulario
- [x] Manejo de errores
- [x] Redirección post-registro

#### 2. ✅ Gestión de Tareas (100%)
- [x] Crear tareas con todos los campos requeridos
- [x] Editar tareas existentes
- [x] Eliminar tareas con confirmación
- [x] Marcar como completada
- [x] Ver lista completa de tareas
- [x] Badges visuales de estado
- [x] Información del sector

#### 3. ⚠️ Filtros y Búsqueda (70%)
- [x] Filtro por estado (endpoint implementado)
- [ ] Búsqueda por palabra clave en UI
- [ ] Interfaz de filtros avanzados
- **Nota**: Los endpoints existen, falta integrar en la interfaz

#### 4. ❌ Notificaciones (0%)
- [ ] Sistema de notificaciones visuales
- [ ] Alertas de fechas próximas a vencer
- **Nota**: Requiere implementación completa

#### 5. ✅ Asociación con Sectores (100%)
- [x] Selector de sectores en formulario
- [x] Visualización del sector en tareas
- [x] Información adicional del sector
- [x] Creación de nuevos sectores

#### 6. ✅ Análisis Geoespacial (100%)
- [x] Promedio de distancia de tareas completadas
- [x] Tarea más cercana al usuario
- [x] Tareas por sector
- [x] Sector con más tareas en 2 km
- [x] Sector con más tareas en 5 km
- [x] Concentración de tareas pendientes
- [x] Visualización en dashboard completo

### Requisitos Técnicos

#### ✅ Completados
- [x] Frontend desarrollado en Vue.js 3
- [x] Componentes reutilizables
- [x] Composition API
- [x] Router para SPA
- [x] Gestión de estado con Pinia
- [x] Cliente HTTP con Axios
- [x] Interceptores JWT
- [x] Guardias de navegación
- [x] CSS moderno con variables
- [x] Responsive design
- [x] Animaciones y transiciones

#### ⚠️ Parciales
- ⚠️ Filtros y búsqueda en UI (70%)
- ⚠️ Manejo de errores robusto (80%)

#### ❌ Pendientes
- [ ] Mapas interactivos (Leaflet integrado pero no usado)
- [ ] Sistema de notificaciones push
- [ ] Tests unitarios
- [ ] Tests E2E
- [ ] Optimización de bundle
- [ ] PWA capabilities
- [ ] Internacionalización (i18n)

---

## 🎨 Características de Diseño

### Variables CSS

El proyecto utiliza un sistema de variables CSS para mantener consistencia:

```css
:root {
  --primary-color: #00d4ff;
  --secondary-color: #1a1a2e;
  --accent-color: #ff6b6b;
  --success: #00ff88;
  --warning: #ffd700;
  --danger: #ff4444;
  --bg-primary: #0f0f1e;
  --bg-secondary: #1a1a2e;
  --text-primary: #ffffff;
  --text-secondary: #a0a0b0;
}
```

### Componentes Reutilizables

- **Navbar**: Barra de navegación con estado de autenticación
- **Card**: Tarjetas para contenido
- **Buttons**: Estilos consistentes para botones primarios, secundarios y de peligro
- **Form elements**: Inputs, textareas y selects estilizados

---

## 🚀 Próximos Pasos

### Prioridad Alta
1. **Implementar filtros en UI**: Agregar barra de filtros en lista de tareas
2. **Búsqueda por palabra clave**: Input de búsqueda funcional
3. **Notificaciones**: Sistema de alertas para fechas de vencimiento

### Prioridad Media
4. **Mapas interactivos**: Integrar Leaflet para visualizar tareas y sectores
5. **Testing**: Agregar tests unitarios con Vitest
6. **Optimización**: Lazy loading de componentes

### Prioridad Baja
7. **PWA**: Convertir en Progressive Web App
8. **Internacionalización**: Soporte multiidioma
9. **Temas**: Permitir cambio entre tema claro/oscuro

---

## 🐛 Problemas Conocidos

1. ~~**Ruta de importación incorrecta en analisis.service.js**~~ ✅ **SOLUCIONADO**
   - Error: `Failed to resolve import "@/modules/core/api/axios"`
   - Solución: Cambiar a `@/core/api/axios`

2. **Falta integración de filtros en UI**
   - Los endpoints existen pero no están conectados en la interfaz

3. **Búsqueda no implementada en frontend**
   - El endpoint `/api/tareas/buscar` existe pero falta el componente

---

## 💡 Mejores Prácticas Implementadas

- ✅ Composition API para mejor reusabilidad
- ✅ Store centralizado con Pinia
- ✅ Interceptores HTTP para JWT automático
- ✅ Guardias de navegación para rutas protegidas
- ✅ Manejo de errores con try-catch
- ✅ Loading states en todas las operaciones asíncronas
- ✅ Confirmaciones antes de acciones destructivas
- ✅ Código modular y organizado por features
- ✅ Nomenclatura consistente en español

---

## 📱 Responsive Breakpoints

- **Desktop**: > 768px
- **Tablet**: 768px - 480px
- **Mobile**: < 480px

Media queries implementadas en:
- Grid de tareas
- Grid de sectores
- Dashboard de análisis
- Formularios

---

## 👥 Autores

Proyecto desarrollado para el curso de Taller de Base de Datos - USACH

---

## 📄 Licencia

Este proyecto es de uso académico.

---

## 📞 Soporte

Para problemas o consultas, crear un issue en el repositorio.

---

## 🔗 Enlaces Útiles

- [Vue.js Documentation](https://vuejs.org/)
- [Vue Router](https://router.vuejs.org/)
- [Pinia](https://pinia.vuejs.org/)
- [Axios](https://axios-http.com/)
- [Leaflet](https://leafletjs.com/)
- [Vite](https://vitejs.dev/)

