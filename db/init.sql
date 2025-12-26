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
    ('admin', '$2a$12$Ndl.ZNvI1pvrJNWPNUUQEOnj.QUy7U5HN9jk1U7MPA.eYSg1BjkrW', ST_SetSRID(ST_MakePoint(-70.6500, -33.4500), 4326));

-- Insertar Tareas de ejemplo con diferentes estados y sectores
INSERT INTO tarea (titulo, descripcion, fecha_vencimiento, estado, id_usuario, id_sector) VALUES
    -- Tareas PENDIENTES
    ('Reunión de proyecto', 'Discutir avances del backend', '2025-12-30', 'PENDIENTE', 1, 1),
    ('Comprar materiales', 'Ir al centro a buscar insumos', '2025-12-28', 'PENDIENTE', 1, 2),
    ('Reparar semáforo', 'Mantenimiento de semáforo en esquina principal', '2026-01-05', 'PENDIENTE', 1, 5),
    ('Revisar alumbrado público', 'Inspección de luminarias en parque', '2026-01-10', 'PENDIENTE', 1, 3),
    ('Reunión con vecinos', 'Organizar junta de vecinos', '2026-01-15', 'PENDIENTE', 1, 6),

    -- Tareas COMPLETADAS (para análisis)
    ('Evento deportivo', 'Asistir a la maratón', '2025-12-20', 'COMPLETADA', 1, 3),
    ('Inspección de calles', 'Revisar estado de pavimento', '2025-12-18', 'COMPLETADA', 1, 2),
    ('Mantenimiento de áreas verdes', 'Poda de árboles', '2025-12-15', 'COMPLETADA', 1, 3),
    ('Reparación de acera', 'Arreglar baldosas rotas', '2025-12-12', 'COMPLETADA', 1, 4),
    ('Control de tráfico', 'Análisis de flujo vehicular', '2025-12-10', 'COMPLETADA', 1, 5),
    ('Limpieza de espacios públicos', 'Retiro de escombros', '2025-12-08', 'COMPLETADA', 1, 6),
    ('Instalación de señalética', 'Colocar señales de tránsito', '2025-12-05', 'COMPLETADA', 1, 7),
    ('Revisión de infraestructura', 'Inspección general', '2025-12-03', 'COMPLETADA', 1, 1),
    ('Mantenimiento de plazas', 'Pintura de bancas', '2025-12-01', 'COMPLETADA', 1, 2),
    ('Control de ruido', 'Medición de contaminación acústica', '2025-11-28', 'COMPLETADA', 1, 8);
