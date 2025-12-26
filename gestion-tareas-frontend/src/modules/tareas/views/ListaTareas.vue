<template>
  <div class="container">
    <div class="header">
      <button class="btn-nueva" @click="$router.push('/tareas/nueva')">➕ Nueva Tarea</button>
    </div>

    <div v-if="loading">Cargando tareas...</div>

    <div v-else class="grid">
      <div v-for="tarea in tareas" :key="tarea.idTarea" class="card task-card">
        <div class="task-header">
          <h3>{{ tarea.titulo }}</h3>
          <span :class="['badge', tarea.estado === 'COMPLETADA' ? 'done' : 'pending']">
            {{ tarea.estado }}
          </span>
        </div>
        <p>{{ tarea.descripcion }}</p>
        <p><small>🗓️ Vence: {{ tarea.fechaVencimiento }}</small></p>
        <p><small>🏢 Sector: {{ tarea.nombreSector }}</small></p>

        <div class="actions">
          <button v-if="tarea.estado !== 'COMPLETADA'" class="btn-completar" @click="completar(tarea.idTarea)">
            ✅ Completar
          </button>
          <button class="btn-eliminar" @click="eliminar(tarea.idTarea)">🗑️ Eliminar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import tareasService from '../api/tareas.service';

const tareas = ref([]);
const loading = ref(true);

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
    await cargarTareas(); // Recargar para ver el cambio
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
    await cargarTareas(); // Recargar para ver el cambio
  } catch (e) {
    console.error('Error al eliminar tarea:', e);
    alert('Error al eliminar la tarea. Por favor, intenta de nuevo.');
  }
};

onMounted(() => {
  cargarTareas();
});
</script>

<style scoped>
.container { padding: 20px; max-width: 1000px; margin: 0 auto; }
.header { display: flex; justify-content: flex-end; align-items: center; margin-bottom: 20px; gap: 10px;}
.grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(300px, 1fr)); gap: 20px; }
.task-header { display: flex; justify-content: space-between; align-items: start; }
.badge { padding: 4px 8px; border-radius: 12px; font-size: 0.8em; color: black; font-weight: bold;}
.badge.done { background-color: #42b883; }
.badge.pending { background-color: #f1c40f; }
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
</style>