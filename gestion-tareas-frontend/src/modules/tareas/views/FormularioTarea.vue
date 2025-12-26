<template>
  <div class="container">
    <div class="form-wrapper">
      <div class="form-header">
        <h2>{{ esEdicion ? '✏️ Editar Tarea' : '📋 Nueva Tarea' }}</h2>
        <p class="subtitle">{{ esEdicion ? 'Modifica los detalles de tu tarea' : 'Crea una nueva tarea y asígnala a un sector' }}</p>
      </div>

      <form @submit.prevent="guardarTarea" class="card form-card">
        <div class="form-group">
          <label>
            <span class="label-icon">✍️</span>
            Título de la Tarea
            <span class="required">*</span>
          </label>
          <input
              v-model="form.titulo"
              required
              placeholder="Ej: Reparar semáforo en Av. Principal"
              maxlength="100"
          />
          <span class="char-count">{{ form.titulo.length }}/100</span>
        </div>

        <div class="form-group">
          <label>
            <span class="label-icon">📝</span>
            Descripción
          </label>
          <textarea
              v-model="form.descripcion"
              rows="4"
              placeholder="Describe los detalles de la tarea..."
              maxlength="500"
          ></textarea>
          <span class="char-count">{{ form.descripcion.length }}/500</span>
        </div>

        <div class="form-row">
          <div class="form-group">
            <label>
              <span class="label-icon">🗓️</span>
              Fecha de Vencimiento
              <span class="required">*</span>
            </label>
            <input
                type="date"
                v-model="form.fechaVencimiento"
                required
                :min="fechaMinima"
            />
          </div>

          <div class="form-group">
            <label>
              <span class="label-icon">🏢</span>
              Sector
              <span class="required">*</span>
            </label>
            <select v-model="form.idSector" required>
              <option value="" disabled>Selecciona un sector</option>
              <option
                  v-for="sector in sectores"
                  :key="sector.idSector"
                  :value="sector.idSector"
              >
                {{ sector.nombre }}
              </option>
            </select>
          </div>
        </div>

        <!-- Información del sector seleccionado -->
        <div v-if="sectorSeleccionado" class="sector-info">
          <div class="info-icon">💡</div>
          <div class="info-content">
            <h4>Sector: {{ sectorSeleccionado.nombre }}</h4>
            <p v-if="sectorSeleccionado.descripcion">{{ sectorSeleccionado.descripcion }}</p>
          </div>
        </div>

        <div class="form-actions">
          <button type="submit" class="btn-primary">
            <span class="icon">{{ esEdicion ? '✅' : '➕' }}</span>
            {{ esEdicion ? 'Actualizar Tarea' : 'Crear Tarea' }}
          </button>
          <button
              type="button"
              class="btn-secondary"
              @click="cancelar"
          >
            <span class="icon">🔙</span>
            Cancelar
          </button>
        </div>
      </form>

      <!-- Vista previa -->
      <div class="card preview-card" v-if="mostrarVista">
        <h3>👁️ Vista Previa</h3>
        <div class="preview-content">
          <div class="preview-item">
            <strong>Título:</strong>
            <span>{{ form.titulo || 'Sin título' }}</span>
          </div>
          <div class="preview-item">
            <strong>Descripción:</strong>
            <span>{{ form.descripcion || 'Sin descripción' }}</span>
          </div>
          <div class="preview-item">
            <strong>Vencimiento:</strong>
            <span>{{ formatearFecha(form.fechaVencimiento) }}</span>
          </div>
          <div class="preview-item">
            <strong>Sector:</strong>
            <span>{{ sectorSeleccionado?.nombre || 'No seleccionado' }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import tareasService from '../api/tareas.service';
import sectoresService from '@/modules/sectores/api/sectores.service';

const router = useRouter();
const route = useRoute();
const sectores = ref([]);
const esEdicion = ref(false);
const mostrarVista = ref(false);

const form = reactive({
  titulo: '',
  descripcion: '',
  fechaVencimiento: '',
  idSector: ''
});

// Fecha mínima = hoy
const fechaMinima = computed(() => {
  const hoy = new Date();
  return hoy.toISOString().split('T')[0];
});

// Sector seleccionado
const sectorSeleccionado = computed(() => {
  if (!form.idSector) return null;
  return sectores.value.find(s => s.idSector === form.idSector);
});

// Watch para mostrar vista previa cuando hay datos
watch(() => [form.titulo, form.descripcion, form.fechaVencimiento, form.idSector], () => {
  mostrarVista.value = form.titulo.length > 0 || form.descripcion.length > 0;
});

onMounted(async () => {
  // Cargar sectores
  try {
    const resp = await sectoresService.obtenerTodos();
    sectores.value = resp.data;
  } catch (e) {
    console.error("Error cargando sectores", e);
    alert('Error al cargar los sectores. Intenta recargar la página.');
  }

  // Verificar si es edición
  if (route.params.id) {
    esEdicion.value = true;
    await cargarTarea(route.params.id);
  }
});

const cargarTarea = async (id) => {
  try {
    // Asumiendo que tienes un endpoint para obtener una tarea por ID
    const resp = await tareasService.obtenerPorId(id);
    const tarea = resp.data;

    form.titulo = tarea.titulo;
    form.descripcion = tarea.descripcion || '';
    form.fechaVencimiento = tarea.fechaVencimiento;
    form.idSector = tarea.idSector;
  } catch (e) {
    console.error("Error cargando tarea", e);
    alert('Error al cargar la tarea. Redirigiendo...');
    router.push('/tareas');
  }
};

const guardarTarea = async () => {
  try {
    if (esEdicion.value) {
      await tareasService.actualizar(route.params.id, form);
      alert('✅ Tarea actualizada exitosamente');
    } else {
      await tareasService.crear(form);
      alert('✅ Tarea creada exitosamente');
    }
    router.push('/tareas');
  } catch (e) {
    console.error("Error al guardar tarea", e);
    alert("❌ Error al guardar: " + (e.response?.data?.message || e.message));
  }
};

const cancelar = () => {
  if (form.titulo || form.descripcion) {
    if (confirm('¿Descartar los cambios?')) {
      router.push('/tareas');
    }
  } else {
    router.push('/tareas');
  }
};

const formatearFecha = (fecha) => {
  if (!fecha) return 'No seleccionada';
  const date = new Date(fecha);
  return date.toLocaleDateString('es-CL', {
    day: '2-digit',
    month: 'long',
    year: 'numeric'
  });
};
</script>

<style scoped>
.container {
  max-width: 900px;
  margin: 30px auto;
  padding: 20px;
}

.form-wrapper {
  display: grid;
  gap: 25px;
}

.form-header {
  text-align: center;
  margin-bottom: 10px;
}

.form-header h2 {
  font-size: 2rem;
  margin-bottom: 8px;
}

.subtitle {
  color: var(--text-secondary);
  font-size: 15px;
}

.form-card {
  padding: 35px;
}

.form-group {
  margin-bottom: 25px;
  position: relative;
}

label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
  color: var(--text-primary);
  font-weight: 600;
  font-size: 14px;
}

.label-icon {
  font-size: 18px;
}

.required {
  color: var(--danger);
  margin-left: 2px;
}

input, textarea, select {
  width: 100%;
  padding: 14px 18px;
  background: var(--bg-secondary);
  border: 2px solid var(--border-color);
  border-radius: 12px;
  color: var(--text-primary);
  font-size: 15px;
  font-family: inherit;
  transition: all 0.3s ease;
}

input:focus, textarea:focus, select:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 4px rgba(0, 212, 255, 0.1);
  background: var(--bg-card);
}

input::placeholder, textarea::placeholder {
  color: var(--text-muted);
}

textarea {
  resize: vertical;
  min-height: 120px;
  line-height: 1.6;
}

.char-count {
  position: absolute;
  bottom: -20px;
  right: 0;
  font-size: 12px;
  color: var(--text-muted);
}

/* Form Row para campos lado a lado */
.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

/* Sector Info */
.sector-info {
  display: flex;
  gap: 15px;
  padding: 20px;
  background: rgba(0, 212, 255, 0.05);
  border: 1px solid rgba(0, 212, 255, 0.2);
  border-radius: 12px;
  margin-bottom: 25px;
  animation: slideIn 0.3s ease-out;
}

@keyframes slideIn {
  from {
    opacity: 0;
    transform: translateY(-10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.info-icon {
  font-size: 28px;
  flex-shrink: 0;
}

.info-content h4 {
  color: var(--primary-color);
  margin-bottom: 5px;
  font-size: 16px;
}

.info-content p {
  color: var(--text-secondary);
  font-size: 14px;
  line-height: 1.5;
}

/* Form Actions */
.form-actions {
  display: flex;
  gap: 15px;
  margin-top: 30px;
  padding-top: 25px;
  border-top: 1px solid var(--border-color);
}

.form-actions button {
  flex: 1;
  padding: 16px;
  font-size: 15px;
  font-weight: 600;
}

.icon {
  margin-right: 8px;
  font-size: 18px;
}

/* Preview Card */
.preview-card {
  padding: 25px;
  background: linear-gradient(135deg, var(--bg-card) 0%, rgba(123, 104, 238, 0.05) 100%);
  border: 1px solid rgba(123, 104, 238, 0.3);
  animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
  from { opacity: 0; }
  to { opacity: 1; }
}

.preview-card h3 {
  margin-bottom: 20px;
  color: var(--accent-color);
}

.preview-content {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.preview-item {
  display: grid;
  grid-template-columns: 140px 1fr;
  gap: 15px;
  padding: 12px;
  background: var(--bg-secondary);
  border-radius: 8px;
  font-size: 14px;
}

.preview-item strong {
  color: var(--text-secondary);
}

.preview-item span {
  color: var(--text-primary);
}

/* Responsive */
@media (max-width: 768px) {
  .container {
    padding: 15px;
  }

  .form-card {
    padding: 25px;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .form-actions {
    flex-direction: column;
  }

  .preview-item {
    grid-template-columns: 1fr;
    gap: 5px;
  }
}
</style>