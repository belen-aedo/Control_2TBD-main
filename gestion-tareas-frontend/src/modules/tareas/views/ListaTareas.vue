<template>
  <div class="container">
    <div class="header">
      <button class="btn-nueva" @click="$router.push('/tareas/nueva')">➕ Nueva Tarea</button>
    </div>

    <!-- Filtros -->
    <div class="filters-section">
      <div class="filter-group">
        <label>Estado:</label>
        <select v-model="filtroEstado" class="filter-select">
          <option value="">Todos</option>
          <option value="PENDIENTE">Pendiente</option>
          <option value="EN_PROCESO">En Proceso</option>
          <option value="COMPLETADA">Completada</option>
          <option value="VENCIDA">Vencida</option>
          <option value="CANCELADA">Cancelada</option>
        </select>
      </div>

      <div class="filter-group">
        <label>Sector:</label>
        <select v-model="filtroSectorLocal" class="filter-select">
          <option value="">Todos</option>
          <option v-for="sector in sectoresUnicos" :key="sector" :value="sector">{{ sector }}</option>
        </select>
      </div>

      <div class="filter-group">
        <label>Distancia máxima (km):</label>
        <input 
          v-model.number="filtroDistancia" 
          type="number" 
          min="0" 
          step="0.5" 
          placeholder="Ejemplo: 5"
          class="filter-input"
        />
      </div>

      <button v-if="hayFiltrosActivos" class="btn-limpiar" @click="limpiarFiltros">
        ✕ Limpiar Filtros
      </button>
    </div>

    <!-- Indicador de filtros activos -->
    <div v-if="hayFiltrosActivos" class="active-filters">
      <span v-if="filtroEstado" class="filter-tag">Estado: {{ filtroEstado }}</span>
      <span v-if="filtroSectorLocal" class="filter-tag">Sector: {{ filtroSectorLocal }}</span>
      <span v-if="filtroDistancia" class="filter-tag">Distancia: ≤ {{ filtroDistancia }} km</span>
    </div>

    <div v-if="loading">Cargando tareas...</div>

    <div v-else>
      <div v-if="tareasFiltradas.length === 0" class="empty">
        {{ hayFiltrosActivos ? 'No hay tareas que coincidan con los filtros.' : 'No hay tareas disponibles.' }}
      </div>

      <div v-else>
        <p class="results-count">Mostrando {{ tareasFiltradas.length }} de {{ tareas.length }} tareas</p>
        <div class="grid">
          <div v-for="tarea in tareasFiltradas" :key="tarea.idTarea" class="card task-card">
            <div class="task-header">
              <h3>{{ tarea.titulo }}</h3>
              <span :class="['badge', getBadgeClass(tarea.estado)]">
                {{ tarea.estado }}
              </span>
            </div>
            <p>{{ tarea.descripcion }}</p>
            <p><small>🗓️ Vence: {{ tarea.fechaVencimiento }}</small></p>
            <p><small>🏢 Sector: {{ tarea.nombreSector }}</small></p>
            <p v-if="tarea.distancia !== null && tarea.distancia !== undefined" class="distance">
              <small>📍 Distancia: {{ tarea.distancia?.toFixed(2) }} km</small>
            </p>

            <div class="actions">
              <button v-if="tarea.estado !== 'COMPLETADA'" class="btn-completar" @click="completar(tarea.idTarea)">
                ✅ Completar
              </button>
              <button class="btn-eliminar" @click="eliminar(tarea.idTarea)">🗑️ Eliminar</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import tareasService from '../api/tareas.service';
import { useRouter, useRoute } from 'vue-router';

const tareas = ref([]);
const loading = ref(true);
const router = useRouter();
const route = useRoute();

// Filtros
const filtroEstado = ref('');
const filtroSectorLocal = ref('');
const filtroDistancia = ref(null);

// Cargar filtro de sector desde URL si existe
const filtroSector = computed(() => route.query.sector || '');

// Sectores únicos para el dropdown
const sectoresUnicos = computed(() => {
  const sectores = [...new Set(tareas.value.map(t => t.nombreSector).filter(Boolean))];
  return sectores.sort();
});

// Verificar si hay filtros activos
const hayFiltrosActivos = computed(() => {
  return filtroEstado.value || filtroSectorLocal.value || filtroDistancia.value;
});

// Función para obtener la clase del badge según el estado
const getBadgeClass = (estado) => {
  const classes = {
    'COMPLETADA': 'done',
    'PENDIENTE': 'pending',
    'EN_PROCESO': 'in-progress',
    'VENCIDA': 'overdue',
    'CANCELADA': 'cancelled'
  };
  return classes[estado] || 'pending';
};

// Tareas filtradas
const tareasFiltradas = computed(() => {
  let resultado = [...tareas.value];

  // Filtro por sector (desde URL o dropdown)
  const sectorFiltro = filtroSectorLocal.value || filtroSector.value;
  if (sectorFiltro) {
    resultado = resultado.filter(t => 
      t.nombreSector?.toLowerCase() === sectorFiltro.toLowerCase()
    );
  }

  // Filtro por estado
  if (filtroEstado.value) {
    resultado = resultado.filter(t => t.estado === filtroEstado.value);
  }

  // Filtro por distancia
  if (filtroDistancia.value && filtroDistancia.value > 0) {
    resultado = resultado.filter(t => {
      // Si la tarea tiene distancia definida, filtrar por ella
      if (t.distancia !== null && t.distancia !== undefined) {
        return t.distancia <= filtroDistancia.value;
      }
      // Si no tiene distancia, incluirla por defecto
      return true;
    });
  }

  return resultado;
});

const cargarTareas = async () => {
  loading.value = true;
  try {
    const resp = await tareasService.obtenerTodas();
    tareas.value = resp.data;
  } catch (e) {
    console.error(e);
  } finally {
    loading.value = false;
  }
};

const completar = async (id) => {
  if(!confirm("¿Marcar como completada?")) return;
  try {
    await tareasService.completar(id);
    await cargarTareas();
    alert('✅ Tarea marcada como completada');
  } catch (e) {
    console.error('Error al completar tarea:', e);
    console.error('Respuesta del servidor:', e.response);
    const mensajeError = e.response?.data?.message || e.response?.data || e.message || 'Error desconocido';
    alert(`Error al completar la tarea: ${mensajeError}`);
  }
};

const eliminar = async (id) => {
  if(!confirm("¿Eliminar tarea?")) return;
  try {
    await tareasService.eliminar(id);
    await cargarTareas();
  } catch (e) {
    console.error('Error al eliminar tarea:', e);
    alert('Error al eliminar la tarea. Por favor, intenta de nuevo.');
  }
};

const limpiarFiltros = () => {
  filtroEstado.value = '';
  filtroSectorLocal.value = '';
  filtroDistancia.value = null;
  router.push('/tareas');
};

onMounted(() => {
  cargarTareas();
  // Si viene filtro de sector desde URL, aplicarlo
  if (filtroSector.value) {
    filtroSectorLocal.value = filtroSector.value;
  }
});
</script>

<style scoped>
.container { padding: 20px; max-width: 1200px; margin: 0 auto; }
.header { display: flex; justify-content: flex-end; align-items: center; margin-bottom: 20px; gap: 10px;}

/* Sección de filtros */
.filters-section {
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  align-items: flex-end;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 6px;
  flex: 1;
  min-width: 150px;
}

.filter-group label {
  font-size: 13px;
  font-weight: 600;
  color: var(--text-secondary);
}

.filter-select,
.filter-input {
  padding: 10px 12px;
  border: 1px solid var(--border-color);
  border-radius: 8px;
  background: var(--bg-secondary);
  color: var(--text-primary);
  font-size: 14px;
  transition: all 0.2s ease;
}

.filter-select:focus,
.filter-input:focus {
  outline: none;
  border-color: #667eea;
  box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
}

.filter-input::placeholder {
  color: var(--text-tertiary);
}

.btn-limpiar {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(240, 147, 251, 0.3);
  white-space: nowrap;
}

.btn-limpiar:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(240, 147, 251, 0.4);
}

.active-filters {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 15px;
}

.filter-tag {
  background: rgba(102, 126, 234, 0.12);
  border: 1px solid rgba(102, 126, 234, 0.3);
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 13px;
  color: var(--text-primary);
  font-weight: 500;
}

.results-count {
  margin-bottom: 15px;
  color: var(--text-secondary);
  font-size: 14px;
  font-weight: 500;
}

.grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; }
.task-header { display: flex; justify-content: space-between; align-items: start; }
.badge { padding: 4px 8px; border-radius: 12px; font-size: 0.8em; color: white; font-weight: bold;}
.badge.done { background-color: #42b883; }
.badge.pending { background-color: #f1c40f; color: #333; }
.badge.in-progress { background-color: #3498db; }
.badge.overdue { background-color: #e74c3c; }
.badge.cancelled { background-color: #95a5a6; }

.distance {
  color: var(--text-secondary);
  font-style: italic;
}

.actions { margin-top: 15px; display: flex; gap: 10px; }

/* Botón Nueva Tarea */
.btn-nueva {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
  font-size: 14px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(102, 126, 234, 0.3);
}

.btn-nueva:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(102, 126, 234, 0.4);
}

/* Botón Completar */
.btn-completar {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  font-size: 13px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(17, 153, 142, 0.3);
}

.btn-completar:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(17, 153, 142, 0.4);
}

/* Botón Eliminar */
.btn-eliminar {
  background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%);
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  font-weight: 500;
  font-size: 13px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 8px rgba(235, 51, 73, 0.3);
}

.btn-eliminar:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(235, 51, 73, 0.4);
}

.empty {
  padding: 18px;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  color: var(--text-secondary);
}
</style>