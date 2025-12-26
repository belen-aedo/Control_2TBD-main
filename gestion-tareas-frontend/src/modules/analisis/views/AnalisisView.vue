<template>
  <div class="container">

    <div v-if="loading" class="loading">
      <div class="spinner"></div>
      <p>Cargando análisis...</p>
    </div>

    <div v-else class="dashboard">
      <!-- Métricas Principales -->
      <div class="metrics-row">
        <!-- Promedio Distancia -->
        <div class="card stat-card highlight">
          <div class="stat-icon-large">📏</div>
          <h3>Promedio de Distancia</h3>
          <div class="big-number">{{ promedioDistancia }} <span class="unit">km</span></div>
          <p class="stat-description">Distancia promedio entre tus tareas completadas y tu ubicación</p>
          <div class="stat-footer">
            <span class="stat-badge">{{ totalCompletadas }} tareas completadas</span>
          </div>
        </div>

        <!-- Tarea Más Cercana -->
        <div class="card stat-card" v-if="tareaCercana">
          <div class="stat-icon-large">🎯</div>
          <h3>Tarea Pendiente Más Cercana</h3>
          <div class="task-info">
            <h4>{{ tareaCercana.titulo }}</h4>
            <div class="distance-badge">
              <span class="distance-value">{{ tareaCercana.distanciaKm?.toFixed(2) }}</span>
              <span class="distance-unit">km</span>
            </div>
          </div>
          <div class="task-meta">
            <div class="meta-item">
              <span>📍</span>
              <span>{{ tareaCercana.nombreSector }}</span>
            </div>
            <div class="meta-item">
              <span>📅</span>
              <span>{{ formatearFecha(tareaCercana.fechaVencimiento) }}</span>
            </div>
          </div>
        </div>

        <!-- Sin tarea cercana -->
        <div class="card stat-card empty-card" v-else>
          <div class="stat-icon-large">🌟</div>
          <h3>¡Excelente Trabajo!</h3>
          <p class="empty-message">No tienes tareas pendientes cercanas</p>
          <div class="success-badge">✨ Todo al día</div>
        </div>
      </div>

      <!-- Distribución por Sector -->
      <div class="card wide-card">
        <div class="card-header">
          <h3>🗺️ Distribución de Tareas por Sector</h3>
          <span class="total-badge">{{ totalTareasPorSector }} tareas totales</span>
        </div>

        <div v-if="porSector.length === 0" class="empty-state-small">
          <p>No hay datos de sectores disponibles</p>
        </div>

        <div v-else class="sectors-grid">
          <div
              v-for="(item, index) in porSector"
              :key="item.nombreSector"
              class="sector-card"
              :style="{ animationDelay: `${index * 0.1}s` }"
          >
            <div class="sector-header">
              <div class="sector-icon">{{ getSectorIcon(item.nombreSector) }}</div>
              <div class="sector-info">
                <h4>{{ item.nombreSector }}</h4>
                <p class="sector-label">Sector</p>
              </div>
            </div>
            <div class="sector-stats">
              <div class="sector-count">
                <span class="count-value">{{ item.cantidadTareas }}</span>
                <span class="count-label">tareas</span>
              </div>
              <div class="progress-bar">
                <div
                    class="progress-fill"
                    :style="{ width: `${getPercentage(item.cantidadTareas)}%` }"
                ></div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- Sector con más tareas en 2km -->
      <div class="card stat-card-horizontal" v-if="sectorMasTareas2km">
        <div class="stat-icon-large">🏆</div>
        <div class="stat-content">
          <h3>Sector Más Activo (2 km)</h3>
          <h4>{{ sectorMasTareas2km.nombreSector }}</h4>
          <div class="stat-details">
            <span class="detail-item">
              <strong>{{ sectorMasTareas2km.cantidadTareas }}</strong> tareas completadas
            </span>
            <span class="detail-separator">•</span>
            <span class="detail-item">En un radio de 2 km</span>
          </div>
        </div>
      </div>

      <!-- Sector con más tareas en 5km -->
      <div class="card stat-card-horizontal" v-if="sectorMasTareas5km">
        <div class="stat-icon-large">🌟</div>
        <div class="stat-content">
          <h3>Sector Más Activo (5 km)</h3>
          <h4>{{ sectorMasTareas5km.nombreSector }}</h4>
          <div class="stat-details">
            <span class="detail-item">
              <strong>{{ sectorMasTareas5km.cantidadTareas }}</strong> tareas completadas
            </span>
            <span class="detail-separator">•</span>
            <span class="detail-item">En un radio de 5 km</span>
          </div>
        </div>
      </div>

      <!-- Concentración de tareas pendientes -->
      <div class="card info-card" v-if="concentracionPendientes.length > 0">
        <h3>📌 Concentración de Tareas Pendientes</h3>
        <p class="info-subtitle">Sectores con mayor cantidad de tareas por completar</p>
        <div class="concentration-list">
          <div
              v-for="sector in concentracionPendientes"
              :key="sector.nombreSector"
              class="concentration-item"
          >
            <div class="concentration-icon">📍</div>
            <div class="concentration-info">
              <span class="concentration-name">{{ sector.nombreSector }}</span>
              <span class="concentration-count">{{ sector.cantidadTareas }} pendientes</span>
            </div>
            <div class="concentration-bar">
              <div
                  class="concentration-fill"
                  :style="{ width: `${getPercentagePendientes(sector.cantidadTareas)}%` }"
              ></div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import analisisService from '../api/analisis.service';

const loading = ref(true);
const promedioDistancia = ref(0);
const totalCompletadas = ref(0);
const tareaCercana = ref(null);
const porSector = ref([]);
const sectorMasTareas2km = ref(null);
const sectorMasTareas5km = ref(null);
const concentracionPendientes = ref([]);

const totalTareasPorSector = computed(() =>
    porSector.value.reduce((sum, item) => sum + item.cantidadTareas, 0)
);

const maxTareasPorSector = computed(() =>
    Math.max(...porSector.value.map(s => s.cantidadTareas), 1)
);

const maxTareasPendientes = computed(() =>
    Math.max(...concentracionPendientes.value.map(s => s.cantidadTareas), 1)
);

onMounted(async () => {
  loading.value = true;

  // Verificar si hay token
  const token = localStorage.getItem('token');
  console.log('Token disponible:', token ? 'Sí' : 'No');

  if (!token) {
    console.error('No hay token disponible');
    loading.value = false;
    return;
  }

  try {
    console.log('Iniciando carga de datos de análisis...');

    // Llamadas principales que siempre deben existir
    const [resPromedio, resCercana, resPorSector, res2km, res5km, resConcentracion] = await Promise.allSettled([
      analisisService.getPromedioDistancia(),
      analisisService.getTareaMasCercana(),
      analisisService.getTareasPorSector(),
      analisisService.getSectorMasTareas2km(),
      analisisService.getSectorMasTareas5km(),
      analisisService.getConcentracionPendientes()
    ]);

    // Log de resultados
    console.log('Resultados de peticiones:');
    console.log('- Promedio:', resPromedio.status);
    console.log('- Cercana:', resCercana.status);
    console.log('- Por Sector:', resPorSector.status);
    console.log('- 2km:', res2km.status);
    console.log('- 5km:', res5km.status);
    console.log('- Concentración:', resConcentracion.status);

    // Procesar promedio distancia - PromedioDistanciaDTO
    if (resPromedio.status === 'fulfilled' && resPromedio.value?.data) {
      const data = resPromedio.value.data;
      promedioDistancia.value = data.promedioDistanciaKm?.toFixed(2) || '0.00';
      totalCompletadas.value = data.totalCompletadas || 0;
      console.log('Promedio cargado:', promedioDistancia.value);
    } else {
      console.log('Sin datos de promedio:', resPromedio.reason?.response?.status || resPromedio.reason?.message);
    }

    // Procesar tarea cercana - TareaCercanaDTO
    if (resCercana.status === 'fulfilled' && resCercana.value?.data) {
      const data = resCercana.value.data;
      if (data.idTarea) {
        tareaCercana.value = data;
        console.log('Tarea cercana:', data.titulo);
      }
    } else {
      console.log('Sin tarea cercana disponible');
    }

    // Procesar tareas por sector - TareasPorSectorDTO[]
    if (resPorSector.status === 'fulfilled' && resPorSector.value?.data) {
      porSector.value = resPorSector.value.data || [];
      console.log('Sectores cargados:', porSector.value.length);
    } else {
      console.log('Sin datos de sectores:', resPorSector.reason?.response?.status || resPorSector.reason?.message);
    }

    // Procesar sector 2km - SectorConTareasDTO
    if (res2km.status === 'fulfilled' && res2km.value?.data) {
      const data = res2km.value.data;
      if (data.nombreSector) {
        sectorMasTareas2km.value = {
          nombreSector: data.nombreSector,
          cantidadTareas: data.cantidadTareasCompletadas || 0,
          distanciaKm: data.distanciaKm || 0
        };
        console.log('Sector 2km:', data.nombreSector);
      }
    } else {
      console.log('Sin datos de sector 2km');
    }

    // Procesar sector 5km - SectorConTareasDTO
    if (res5km.status === 'fulfilled' && res5km.value?.data) {
      const data = res5km.value.data;
      if (data.nombreSector) {
        sectorMasTareas5km.value = {
          nombreSector: data.nombreSector,
          cantidadTareas: data.cantidadTareasCompletadas || 0,
          distanciaKm: data.distanciaKm || 0
        };
        console.log('Sector 5km:', data.nombreSector);
      }
    } else {
      console.log('Sin datos de sector 5km');
    }

    // Procesar concentración pendientes - ConcentracionTareasDTO[]
    if (resConcentracion.status === 'fulfilled' && resConcentracion.value?.data) {
      const data = resConcentracion.value.data || [];
      concentracionPendientes.value = data.map(item => ({
        nombreSector: item.nombreSector,
        cantidadTareas: item.cantidadTareasPendientes || 0,
        latitud: item.latitud,
        longitud: item.longitud
      }));
      console.log('Concentración pendientes:', concentracionPendientes.value.length);
    } else {
      console.log('Sin datos de concentración');
    }

    console.log('✅ Carga de análisis completada');

  } catch (e) {
    console.error("❌ Error cargando análisis:", e);
    console.error("Detalles:", e.response?.data || e.message);

    // Manejar errores de autenticación
    if (e.response?.status === 401 || e.response?.status === 403) {
      console.error('Error de autenticación - tu sesión ha expirado o no tienes permisos');
      localStorage.removeItem('token');
      localStorage.removeItem('usuario');
      window.location.href = '/login';
    } else if (e.response?.status !== 404) {
      alert('Error al cargar el análisis. Verifica que el backend esté corriendo y que tengas tareas registradas.');
    }
  } finally {
    loading.value = false;
  }
});

const getPercentage = (cantidad) => {
  return (cantidad / maxTareasPorSector.value) * 100;
};

const getPercentagePendientes = (cantidad) => {
  return (cantidad / maxTareasPendientes.value) * 100;
};

const getSectorIcon = (nombreSector) => {
  const iconos = {
    'construccion': '🏗️',
    'construcción': '🏗️',
    'semaforos': '🚦',
    'semáforos': '🚦',
    'calles': '🛣️',
    'parques': '🌳',
    'alumbrado': '💡',
    'alcantarillado': '🚰',
    'default': '📌'
  };

  const key = nombreSector.toLowerCase();
  return iconos[key] || iconos.default;
};

const formatearFecha = (fecha) => {
  if (!fecha) return 'Sin fecha';
  const date = new Date(fecha);
  return date.toLocaleDateString('es-CL', {
    day: '2-digit',
    month: 'short',
    year: 'numeric'
  });
};
</script>

<style scoped>

/* Dashboard Layout */
.dashboard {
  display: grid;
  gap: 25px;
}

.metrics-row {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 20px;
}

/* Stat Cards */
.stat-card {
  padding: 30px;
  text-align: center;
  position: relative;
  overflow: hidden;
}

.stat-card.highlight {
  background: linear-gradient(135deg, var(--bg-card) 0%, rgba(0, 212, 255, 0.1) 100%);
  border: 2px solid var(--primary-color);
}

.stat-icon-large {
  font-size: 48px;
  margin-bottom: 15px;
  display: inline-block;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0px); }
  50% { transform: translateY(-10px); }
}

.stat-card h3 {
  font-size: 16px;
  color: var(--text-secondary);
  margin-bottom: 15px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.big-number {
  font-size: 4rem;
  font-weight: 800;
  background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
  margin: 20px 0;
  line-height: 1;
}

.unit {
  font-size: 2rem;
  opacity: 0.7;
}

.stat-description {
  color: var(--text-secondary);
  font-size: 14px;
  margin-top: 15px;
  line-height: 1.5;
}

.stat-footer {
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid var(--border-color);
}

.stat-badge {
  background: rgba(0, 212, 255, 0.15);
  color: var(--primary-color);
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
}

/* Task Info */
.task-info {
  margin: 20px 0;
}

.task-info h4 {
  font-size: 20px;
  margin-bottom: 15px;
  color: var(--text-primary);
}

.distance-badge {
  display: inline-flex;
  align-items: baseline;
  gap: 5px;
  background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
  padding: 12px 24px;
  border-radius: 12px;
  margin: 10px 0;
}

.distance-value {
  font-size: 32px;
  font-weight: 800;
  color: white;
}

.distance-unit {
  font-size: 18px;
  color: white;
  opacity: 0.9;
}

.task-meta {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-top: 20px;
  padding: 15px;
  background: var(--bg-secondary);
  border-radius: 10px;
}

.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  color: var(--text-secondary);
}

/* Empty Card */
.empty-card {
  background: linear-gradient(135deg, rgba(0, 255, 136, 0.1), rgba(0, 212, 255, 0.05));
  border-color: var(--success);
}

.empty-message {
  color: var(--text-secondary);
  font-size: 15px;
  margin-top: 10px;
}

.success-badge {
  margin-top: 15px;
  padding: 8px 20px;
  background: linear-gradient(135deg, var(--success), #00cc77);
  color: white;
  border-radius: 20px;
  font-size: 13px;
  font-weight: 600;
  display: inline-block;
}

/* Wide Card - Sectores */
.wide-card {
  grid-column: 1 / -1;
  padding: 30px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  flex-wrap: wrap;
  gap: 15px;
}

.total-badge {
  background: var(--bg-secondary);
  padding: 8px 16px;
  border-radius: 20px;
  font-size: 13px;
  color: var(--text-secondary);
  border: 1px solid var(--border-color);
}

.sectors-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 15px;
}

.sector-card {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 20px;
  transition: all 0.3s ease;
  animation: slideUp 0.5s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.sector-card:hover {
  transform: translateY(-3px);
  border-color: var(--primary-color);
  box-shadow: 0 8px 20px rgba(0, 212, 255, 0.2);
}

.sector-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 15px;
}

.sector-icon {
  font-size: 32px;
  width: 50px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 212, 255, 0.1);
  border-radius: 10px;
}

.sector-info h4 {
  font-size: 16px;
  margin: 0 0 4px 0;
  color: var(--text-primary);
}

.sector-label {
  font-size: 12px;
  color: var(--text-muted);
  margin: 0;
}

.sector-stats {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.sector-count {
  display: flex;
  align-items: baseline;
  gap: 6px;
}

.count-value {
  font-size: 28px;
  font-weight: 700;
  color: var(--primary-color);
}

.count-label {
  font-size: 13px;
  color: var(--text-secondary);
}

.progress-bar {
  height: 6px;
  background: var(--bg-primary);
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--primary-color), var(--accent-color));
  transition: width 0.5s ease;
  border-radius: 3px;
}

/* Horizontal Stat Card */
.stat-card-horizontal {
  display: flex;
  align-items: center;
  gap: 25px;
  padding: 25px;
  text-align: left;
}

.stat-content {
  flex: 1;
}

.stat-content h3 {
  font-size: 14px;
  color: var(--text-secondary);
  margin-bottom: 8px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.stat-content h4 {
  font-size: 24px;
  color: var(--primary-color);
  margin-bottom: 12px;
}

.stat-details {
  display: flex;
  align-items: center;
  gap: 10px;
  flex-wrap: wrap;
  color: var(--text-secondary);
  font-size: 14px;
}

.detail-separator {
  opacity: 0.5;
}

/* Info Card - Concentración */
.info-card {
  padding: 30px;
}

.info-card h3 {
  margin-bottom: 8px;
}

.info-subtitle {
  color: var(--text-secondary);
  font-size: 14px;
  margin-bottom: 20px;
}

.concentration-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.concentration-item {
  display: grid;
  grid-template-columns: auto 1fr auto;
  align-items: center;
  gap: 15px;
  padding: 15px;
  background: var(--bg-secondary);
  border-radius: 10px;
  border: 1px solid var(--border-color);
  transition: all 0.3s ease;
}

.concentration-item:hover {
  border-color: var(--primary-color);
  transform: translateX(5px);
}

.concentration-icon {
  font-size: 24px;
}

.concentration-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.concentration-name {
  font-weight: 600;
  color: var(--text-primary);
}

.concentration-count {
  font-size: 13px;
  color: var(--text-secondary);
}

.concentration-bar {
  width: 120px;
  height: 8px;
  background: var(--bg-primary);
  border-radius: 4px;
  overflow: hidden;
}

.concentration-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--warning), var(--primary-color));
  transition: width 0.5s ease;
  border-radius: 4px;
}

/* Loading */
.loading {
  text-align: center;
  padding: 80px 20px;
}

.spinner {
  width: 60px;
  height: 60px;
  border: 5px solid var(--border-color);
  border-top-color: var(--primary-color);
  border-radius: 50%;
  margin: 0 auto 25px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

/* Empty State */
.empty-state-small {
  text-align: center;
  padding: 40px 20px;
  color: var(--text-secondary);
}

@media (max-width: 768px) {
  .metrics-row {
    grid-template-columns: 1fr;
  }

  .sectors-grid {
    grid-template-columns: 1fr;
  }

  .stat-card-horizontal {
    flex-direction: column;
    text-align: center;
  }
}
</style>