-- =========================================================
-- 1. LIMPIEZA PREVIA
-- =========================================================
DROP TABLE IF EXISTS tarea CASCADE;
DROP TABLE IF EXISTS sector CASCADE;
DROP TABLE IF EXISTS usuario CASCADE;

-- =========================================================
-- 2. CONFIGURACIÓN
-- =========================================================
CREATE EXTENSION IF NOT EXISTS postgis;

-- =========================================================
-- 3. CREACIÓN DE TABLAS
-- =========================================================

-- Tabla Usuario
CREATE TABLE usuario (
                         id_usuario BIGSERIAL PRIMARY KEY,
                         nombre_usuario VARCHAR(255) UNIQUE NOT NULL,
                         contrasena VARCHAR(255) NOT NULL,
                         ubicacion_geografica GEOMETRY(Point, 4326) NOT NULL
);

-- Tabla Sector
CREATE TABLE sector (
                        id_sector BIGSERIAL PRIMARY KEY,
                        nombre VARCHAR(255) NOT NULL,
                        ubicacion_espacial GEOMETRY(Point, 4326) NOT NULL
);

-- Tabla Tarea
CREATE TABLE tarea (
                       id_tarea BIGSERIAL PRIMARY KEY,
                       titulo VARCHAR(255) NOT NULL,
                       descripcion TEXT,
                       fecha_vencimiento DATE,
                       estado VARCHAR(50) DEFAULT 'PENDIENTE',
                       id_usuario BIGINT NOT NULL,
                       id_sector BIGINT NOT NULL,
                       CONSTRAINT fk_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
                       CONSTRAINT fk_sector FOREIGN KEY (id_sector) REFERENCES sector(id_sector)
);

-- =========================================================
-- 4. ÍNDICES (Para que PostGIS funcione rápido)
-- =========================================================
CREATE INDEX idx_usuario_geom ON usuario USING GIST(ubicacion_geografica);
CREATE INDEX idx_sector_geom ON sector USING GIST(ubicacion_espacial);

-- =========================================================
-- 5. DATOS DE PRUEBA
-- =========================================================

-- Insertar Sectores (más variedad)
INSERT INTO sector (nombre, ubicacion_espacial) VALUES
    ('Campus USACH', ST_SetSRID(ST_MakePoint(-70.6836, -33.4497), 4326)),
    ('Plaza de Armas', ST_SetSRID(ST_MakePoint(-70.6506, -33.4372), 4326)),
    ('Parque O''Higgins', ST_SetSRID(ST_MakePoint(-70.6611, -33.4639), 4326)),
    ('Costanera Center', ST_SetSRID(ST_MakePoint(-70.6070, -33.4174), 4326)),
    ('Estación Central', ST_SetSRID(ST_MakePoint(-70.6822, -33.4594), 4326)),
    ('Barrio Yungay', ST_SetSRID(ST_MakePoint(-70.6700, -33.4450), 4326)),
    ('Providencia', ST_SetSRID(ST_MakePoint(-70.6170, -33.4320), 4326)),
    ('Las Condes', ST_SetSRID(ST_MakePoint(-70.5800, -33.4100), 4326));

-- Insertar Usuario "admin" con contraseña "1234"
-- Ubicación: Centro de Santiago
INSERT INTO usuario (nombre_usuario, contrasena, ubicacion_geografica) VALUES
    ('admin', '$2a$12$Ndl.ZNvI1pvrJNWPNUUQEOnj.QUy7U5HN9jk1U7MPA.eYSg1BjkrW', ST_SetSRID(ST_MakePoint(-70.6500, -33.4500), 4326)),
    ('supervisor', '$2a$12$Ndl.ZNvI1pvrJNWPNUUQEOnj.QUy7U5HN9jk1U7MPA.eYSg1BjkrW', ST_SetSRID(ST_MakePoint(-70.6200, -33.4300), 4326)),
    ('operador1', '$2a$12$Ndl.ZNvI1pvrJNWPNUUQEOnj.QUy7U5HN9jk1U7MPA.eYSg1BjkrW', ST_SetSRID(ST_MakePoint(-70.6800, -33.4700), 4326)),
    ('operador2', '$2a$12$Ndl.ZNvI1pvrJNWPNUUQEOnj.QUy7U5HN9jk1U7MPA.eYSg1BjkrW', ST_SetSRID(ST_MakePoint(-70.5900, -33.4000), 4326)),
    ('analista', '$2a$12$Ndl.ZNvI1pvrJNWPNUUQEOnj.QUy7U5HN9jk1U7MPA.eYSg1BjkrW', ST_SetSRID(ST_MakePoint(-70.6400, -33.5200), 4326));

-- Insertar Tareas de ejemplo con diferentes estados y sectores
INSERT INTO tarea (titulo, descripcion, fecha_vencimiento, estado, id_usuario, id_sector) VALUES
    -- Tareas PENDIENTES (admin)
    ('Reunión de proyecto', 'Discutir avances del backend', '2025-12-30', 'PENDIENTE', 1, 1),
    ('Comprar materiales', 'Ir al centro a buscar insumos', '2025-12-28', 'PENDIENTE', 1, 2),
    ('Reparar semáforo', 'Mantenimiento de semáforo en esquina principal', '2026-01-05', 'PENDIENTE', 1, 5),
    ('Revisar alumbrado público', 'Inspección de luminarias en parque', '2026-01-10', 'PENDIENTE', 1, 3),
    ('Reunión con vecinos', 'Organizar junta de vecinos', '2026-01-15', 'PENDIENTE', 1, 6),

    -- Tareas COMPLETADAS (admin)
    ('Evento deportivo', 'Asistir a la maratón', '2025-12-20', 'COMPLETADA', 1, 3),
    ('Inspección de calles', 'Revisar estado de pavimento', '2025-12-18', 'COMPLETADA', 1, 2),
    ('Mantenimiento de áreas verdes', 'Poda de árboles', '2025-12-15', 'COMPLETADA', 1, 3),
    ('Reparación de acera', 'Arreglar baldosas rotas', '2025-12-12', 'COMPLETADA', 1, 4),
    ('Control de tráfico', 'Análisis de flujo vehicular', '2025-12-10', 'COMPLETADA', 1, 5),
    ('Limpieza de espacios públicos', 'Retiro de escombros', '2025-12-08', 'COMPLETADA', 1, 6),
    ('Instalación de señalética', 'Colocar señales de tránsito', '2025-12-05', 'COMPLETADA', 1, 7),
    ('Revisión de infraestructura', 'Inspección general', '2025-12-03', 'COMPLETADA', 1, 1),
    ('Mantenimiento de plazas', 'Pintura de bancas', '2025-12-01', 'COMPLETADA', 1, 2),
    ('Control de ruido', 'Medición de contaminación acústica', '2025-11-28', 'COMPLETADA', 1, 8),

    -- Supervisor (en proceso y pendientes)
    ('Plan de evacuación', 'Actualizar protocolos de emergencia', '2026-01-08', 'EN_PROCESO', 2, 4),
    ('Capacitación brigada', 'Entrenamiento en primeros auxilios', '2026-01-12', 'PENDIENTE', 2, 1),
    ('Revisión de CCTV', 'Monitorear cámaras en sector comercial', '2026-01-06', 'EN_PROCESO', 2, 2),
    ('Informe mensual', 'Consolidar métricas de mantenimiento', '2026-01-20', 'PENDIENTE', 2, 7),

    -- Operador 1 (mixto de estados)
    ('Reposición de luminarias', 'Cambiar focos quemados en plaza', '2026-01-04', 'EN_PROCESO', 3, 6),
    ('Mantención de ciclovía', 'Reparar baches y señalética', '2026-01-09', 'PENDIENTE', 3, 5),
    ('Poda preventiva', 'Retiro de ramas peligrosas', '2025-12-29', 'COMPLETADA', 3, 3),
    ('Limpieza de canaletas', 'Retiro de hojas antes de lluvias', '2026-01-02', 'PENDIENTE', 3, 8),

    -- Operador 2 (atrasadas y canceladas)
    ('Revisión de bombas de agua', 'Chequeo de presión en subestación', '2025-12-22', 'VENCIDA', 4, 4),
    ('Reemplazo de barandas', 'Barandas oxidadas en pasarela', '2026-01-18', 'PENDIENTE', 4, 2),
    ('Sellado de fugas', 'Perdida menor en matriz', '2025-12-24', 'COMPLETADA', 4, 1),
    ('Despeje de escombros', 'Restos de obra en calzada', '2025-12-27', 'CANCELADA', 4, 6),

    -- Analista (tareas para dashboards)
    ('Reporte de SLA', 'Calcular cumplimiento por sector', '2026-01-07', 'EN_PROCESO', 5, 7),
    ('Mapa de calor de incidencias', 'Generar visualización espacial', '2026-01-11', 'PENDIENTE', 5, 3),
    ('Auditoría de datos', 'Validar duplicados en base histórica', '2026-01-03', 'COMPLETADA', 5, 5),
    ('Encuesta de satisfacción', 'Levantar feedback de vecinos', '2026-01-14', 'PENDIENTE', 5, 8),

    -- Lote masivo para pruebas de carga y análisis (69 tareas adicionales aprox.)
    ('Patrullaje nocturno', 'Recorrido de seguridad en sector', '2025-12-26', 'PENDIENTE', 2, 6),
    ('Control de plagas', 'Fumigación en plaza central', '2026-01-16', 'EN_PROCESO', 3, 2),
    ('Revisión de hidrantes', 'Chequeo de presión y señalización', '2026-01-13', 'PENDIENTE', 2, 5),
    ('Inventario de bodegas', 'Contar herramientas disponibles', '2025-12-30', 'COMPLETADA', 4, 7),
    ('Pintura de pasos peatonales', 'Repintar cruces desgastados', '2026-01-09', 'EN_PROCESO', 3, 1),
    ('Reemplazo de luminarias LED', 'Actualizar postes antiguos', '2026-01-21', 'PENDIENTE', 3, 4),
    ('Calibración de sensores', 'Sensores ambientales en parque', '2026-01-05', 'COMPLETADA', 5, 3),
    ('Visita a vecinos', 'Levantar requerimientos del barrio', '2026-01-18', 'PENDIENTE', 1, 6),
    ('Monitoreo de ruido', 'Mediciones en zona comercial', '2025-12-29', 'COMPLETADA', 5, 2),
    ('Retiro de escombros pesados', 'Demolición menor en avenida', '2026-01-04', 'PENDIENTE', 4, 5),
    ('Limpieza de drenajes', 'Prevención de inundaciones', '2026-01-03', 'EN_PROCESO', 3, 8),
    ('Revisión de señalética vial', 'Corregir señales dañadas', '2026-01-08', 'PENDIENTE', 2, 1),
    ('Podado estacional', 'Poda de temporada en parques', '2026-01-19', 'PENDIENTE', 3, 3),
    ('Chequeo de cámaras', 'Mantenimiento de CCTV', '2026-01-06', 'COMPLETADA', 2, 4),
    ('Despeje de veredas', 'Eliminar obstáculos en veredas', '2026-01-02', 'PENDIENTE', 4, 6),
    ('Mantenimiento de juegos infantiles', 'Revisión de seguridad', '2026-01-17', 'PENDIENTE', 1, 3),
    ('Control de maleza', 'Aplicar herbicida en zonas verdes', '2026-01-12', 'EN_PROCESO', 3, 7),
    ('Reparación de bancas', 'Cambiar tablas dañadas', '2026-01-15', 'PENDIENTE', 3, 2),
    ('Inspección de puentes peatonales', 'Verificar corrosión', '2026-01-22', 'PENDIENTE', 4, 4),
    ('Plan de contingencia climática', 'Preparar ante lluvias intensas', '2026-01-05', 'COMPLETADA', 2, 8),
    ('Revisión de generadores', 'Probar respaldo eléctrico', '2026-01-07', 'EN_PROCESO', 4, 1),
    ('Capacitación de cuadrillas', 'Seguridad en altura', '2026-01-09', 'PENDIENTE', 2, 5),
    ('Verificación de barandas', 'Chequeo de fijaciones', '2026-01-04', 'COMPLETADA', 4, 2),
    ('Censo de luminarias', 'Contar puntos de luz activos', '2026-01-14', 'PENDIENTE', 5, 6),
    ('Inspección de paraderos', 'Evaluar techos y asientos', '2026-01-10', 'EN_PROCESO', 3, 1),
    ('Reasfalto menor', 'Parcheo de baches críticos', '2026-01-11', 'PENDIENTE', 4, 5),
    ('Limpieza de grafitis', 'Remover pintura en muros', '2026-01-03', 'COMPLETADA', 1, 7),
    ('Ordenamiento de bodegas', 'Etiquetar insumos', '2026-01-06', 'PENDIENTE', 4, 3),
    ('Prueba de alarmas', 'Ensayo de sirenas comunitarias', '2026-01-08', 'EN_PROCESO', 2, 6),
    ('Sustitución de tapas de alcantarilla', 'Tapa faltante en esquina', '2026-01-02', 'PENDIENTE', 4, 4),
    ('Mantenimiento de postes', 'Ajuste de pernos y bases', '2026-01-12', 'PENDIENTE', 3, 2),
    ('Recolección de residuos voluminosos', 'Muebles abandonados', '2026-01-15', 'PENDIENTE', 1, 5),
    ('Actualización de cartografía', 'Refrescar capas GIS', '2026-01-09', 'COMPLETADA', 5, 8),
    ('Instalación de bicicleteros', 'Nuevos racks en plaza', '2026-01-18', 'PENDIENTE', 3, 6),
    ('Chequeo de drenajes pluviales', 'Obstrucción por hojas', '2026-01-05', 'EN_PROCESO', 4, 1),
    ('Campaña informativa', 'Volantes de seguridad', '2026-01-07', 'PENDIENTE', 2, 7),
    ('Prueba de backup de datos', 'Restauración de base', '2026-01-04', 'COMPLETADA', 5, 3),
    ('Revisión de escalas de edificios', 'Estado de pasamanos', '2026-01-13', 'PENDIENTE', 4, 2),
    ('Limpieza de lagunas artificiales', 'Retiro de algas', '2026-01-16', 'EN_PROCESO', 3, 3),
    ('Ajuste de temporizadores', 'Iluminación nocturna', '2026-01-20', 'PENDIENTE', 3, 5),
    ('Prueba de radios', 'Verificar comunicación interna', '2026-01-02', 'COMPLETADA', 2, 4),
    ('Reemplazo de bombines', 'Puertas de acceso técnico', '2026-01-11', 'PENDIENTE', 4, 6),
    ('Control de inventario crítico', 'Repuestos de emergencia', '2026-01-14', 'EN_PROCESO', 2, 1),
    ('Restauración de murales', 'Pintura comunitaria', '2026-01-22', 'PENDIENTE', 1, 7),
    ('Puesta a tierra', 'Mediciones de seguridad eléctrica', '2026-01-08', 'COMPLETADA', 4, 5),
    ('Chequeo de sumideros', 'Limpieza preventiva', '2026-01-03', 'PENDIENTE', 3, 8),
    ('Mantenimiento de parques biosaludables', 'Engrase y apriete', '2026-01-17', 'PENDIENTE', 3, 3),
    ('Inspección de techos livianos', 'Filtraciones detectadas', '2026-01-10', 'EN_PROCESO', 4, 2),
    ('Rotulación de bodegas', 'Señalética interna', '2026-01-06', 'PENDIENTE', 4, 7),
    ('Auditoría de accesos', 'Revisar logs y permisos', '2026-01-05', 'COMPLETADA', 5, 4),
    ('Refuerzo de barandas', 'Soldadura y pintura', '2026-01-12', 'PENDIENTE', 4, 6),
    ('Revisión de tableros eléctricos', 'Ajuste de breakers', '2026-01-04', 'EN_PROCESO', 4, 1),
    ('Monitoreo de aire', 'Partículas y gases', '2026-01-09', 'PENDIENTE', 5, 5),
    ('Actualización de protocolos', 'Nueva normativa', '2026-01-07', 'COMPLETADA', 2, 2),
    ('Visado de contratos', 'Revisión legal rápida', '2026-01-13', 'PENDIENTE', 5, 7),
    ('Chequeo de iluminación peatonal', 'Tramos oscuros', '2026-01-15', 'PENDIENTE', 3, 6),
    ('Reemplazo de tapas de registro', 'Fisuras detectadas', '2026-01-18', 'EN_PROCESO', 4, 5),
    ('Pintura de líneas de estacionamiento', 'Sector municipal', '2026-01-11', 'PENDIENTE', 3, 4),
    ('Prueba de UPS', 'Corte simulado', '2026-01-06', 'COMPLETADA', 5, 1),
    ('Limpieza de pozos', 'Sedimentos acumulados', '2026-01-03', 'PENDIENTE', 4, 8),
    ('Optimización de rutas', 'Asignación de cuadrillas', '2026-01-09', 'EN_PROCESO', 5, 6),
    ('Validación de sensores de humo', 'Alarmas comunitarias', '2026-01-05', 'COMPLETADA', 2, 3),
    ('Revisión de papeleros', 'Reposición y limpieza', '2026-01-14', 'PENDIENTE', 1, 2),
    ('Control de vegetación en calzadas', 'Corte de ramas bajas', '2026-01-12', 'PENDIENTE', 3, 5),
    ('Inspección de ductos de ventilación', 'Limpieza y filtros', '2026-01-16', 'EN_PROCESO', 4, 7),
    ('Levantamiento fotográfico', 'Registro de incidencias', '2026-01-08', 'COMPLETADA', 5, 8),
    ('Reemplazo de cables sueltos', 'Seguridad peatonal', '2026-01-10', 'PENDIENTE', 4, 6),
    ('Corte de pasto extensivo', 'Grandes áreas verdes', '2026-01-15', 'PENDIENTE', 3, 3),
    ('Inspección de válvulas', 'Operatividad en red de agua', '2026-01-11', 'EN_PROCESO', 4, 4),
    ('Toma de encuestas ciudadanas', 'Priorizar proyectos', '2026-01-13', 'PENDIENTE', 1, 1),
    ('Plan de mejoras trimestral', 'Roadmap de mantenimiento', '2026-01-20', 'PENDIENTE', 2, 5),
    ('Balance de carga eléctrica', 'Distribución en tableros', '2026-01-07', 'COMPLETADA', 4, 2),
    ('Supervisión de obras menores', 'Control de contratistas', '2026-01-09', 'EN_PROCESO', 2, 4),
    ('Reposición de tapas de luz', 'Caja de conexión expuesta', '2026-01-04', 'PENDIENTE', 3, 6),
    ('Limpieza de canales', 'Retiro de basura flotante', '2026-01-12', 'PENDIENTE', 4, 8),
    ('Test de resiliencia TI', 'Prueba de carga de sistemas', '2026-01-06', 'COMPLETADA', 5, 1),
    ('Verificación de sensores de tráfico', 'Sincronización de semáforos', '2026-01-08', 'PENDIENTE', 5, 5),
    ('Checklist de seguridad industrial', 'EPP y protocolos', '2026-01-10', 'EN_PROCESO', 2, 7),
    ('Actualización de planos', 'Planos As-Built recientes', '2026-01-14', 'PENDIENTE', 5, 3),
    ('Revisión de ductos de aguas lluvias', 'Obstrucciones menores', '2026-01-11', 'PENDIENTE', 4, 4),
    ('Mantenimiento de bombas de achique', 'Prueba de arranque', '2026-01-05', 'COMPLETADA', 4, 5),
    ('Refuerzo de iluminación en parques', 'Zonas críticas', '2026-01-17', 'PENDIENTE', 3, 6),
    ('Pilotaje de app de reportes', 'Usuarios internos', '2026-01-09', 'EN_PROCESO', 5, 7),
    ('Control de compactación de relleno', 'Obra en ejecución', '2026-01-13', 'PENDIENTE', 2, 2),
    ('Retiro de carteles ilegales', 'Postes y muros', '2026-01-04', 'COMPLETADA', 1, 1),
    ('Seguimiento de incidencias históricas', 'Curar base de datos', '2026-01-07', 'PENDIENTE', 5, 8),
    ('Capacitación en GIS', 'Uso de mapas para cuadrillas', '2026-01-15', 'PENDIENTE', 5, 6),
    ('Análisis de riesgos operacionales', 'Matriz de riesgos', '2026-01-12', 'EN_PROCESO', 2, 3),
    ('Revisión de extintores', 'Vencimientos y presión', '2026-01-10', 'COMPLETADA', 4, 7),
    ('Plan de podas críticas', 'Árboles con riesgo de caída', '2026-01-18', 'PENDIENTE', 3, 3),
    ('Optimización de rutas de recolección', 'Reducción de tiempos', '2026-01-14', 'PENDIENTE', 5, 2),
    ('Auditoría de llaves maestras', 'Control de acceso físico', '2026-01-06', 'COMPLETADA', 2, 4),
    ('Prueba de sistemas de riego', 'Programadores y válvulas', '2026-01-11', 'PENDIENTE', 3, 5),
    ('Revisión de pasarelas peatonales', 'Desgaste de superficies', '2026-01-16', 'EN_PROCESO', 4, 6),
    ('Acta de cierre de proyectos', 'Formalizar entregas', '2026-01-08', 'PENDIENTE', 2, 7),
    ('Revisión de puntos de wifi', 'Cobertura en plazas', '2026-01-05', 'COMPLETADA', 5, 1);

