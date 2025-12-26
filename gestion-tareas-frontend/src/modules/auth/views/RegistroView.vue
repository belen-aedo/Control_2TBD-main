<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="auth-header">
        <h2>REGISTRO</h2>
      </div>

      <form @submit.prevent="handleRegistro">
        <div class="input-group">
          <input
              v-model="form.nombreUsuario"
              type="text"
              placeholder="Usuario"
              required
          />
        </div>

        <div class="input-group">
          <input
              v-model="form.contrasena"
              type="password"
              placeholder="Contraseña"
              required
          />
        </div>

        <div class="map-section">
          <label class="map-label">Selecciona tu ubicación (Haz clic en el mapa):</label>
          <div id="mapaRegistro" class="map-container"></div>

          <div v-if="form.latitud" class="coords-container">
            <div class="coord-item">
              <span class="coord-label">LAT</span>
              <span class="coord-value">{{ form.latitud.toFixed(5) }}</span>
            </div>
            <div class="coord-divider"></div>
            <div class="coord-item">
              <span class="coord-label">LNG</span>
              <span class="coord-value">{{ form.longitud.toFixed(5) }}</span>
            </div>
          </div>

        </div>

        <button type="submit" class="btn-register">REGISTRARSE</button>

        <p v-if="error" class="error">{{ error }}</p>
      </form>

      <div class="footer-link">
        <button @click="$router.push('/login')" class="btn-link">
          ¿Ya tienes cuenta? Inicia sesión
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue';
import L from 'leaflet';
import authService from '../api/auth.service';
import { useRouter } from 'vue-router';

const router = useRouter();
const form = reactive({
  nombreUsuario: '',
  contrasena: '',
  latitud: null,
  longitud: null
});
const error = ref('');
let map = null;
let marker = null;

onMounted(() => {
  map = L.map('mapaRegistro').setView([-33.4500, -70.6500], 12);
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);

  map.on('click', (e) => {
    const { lat, lng } = e.latlng;
    form.latitud = lat;
    form.longitud = lng;

    if (marker) map.removeLayer(marker);
    marker = L.marker([lat, lng]).addTo(map);
  });
});

const handleRegistro = async () => {
  if (!form.latitud) {
    error.value = "Debes seleccionar una ubicación en el mapa";
    return;
  }
  try {
    await authService.registro(form);
    router.push('/login');
  } catch (e) {
    error.value = "Error al registrar. El usuario quizás ya existe.";
  }
};
</script>

<style scoped>
.auth-page {
  width: 100vw; /* Asegura ancho completo */
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
  url('@/assets/images/login-bg.jpg');
  background-size: cover;
  background-position: center;
  /* background-attachment: fixed; */ /* Opcional: comentar si da problemas en móvil */
  padding: 20px;
}

.auth-card {
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 20px;
  padding: 35px 30px;
  width: 100%;
  max-width: 500px;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3);
  max-height: 90vh;
  overflow-y: auto;
}

.auth-header {
  text-align: center;
  margin-bottom: 25px;
}

.auth-header h2 {
  color: white;
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 1px;
  margin: 0;
}

.input-group {
  margin-bottom: 18px;
}

.input-group input {
  width: 100%;
  padding: 15px 20px;
  background: rgba(255, 255, 255, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 50px;
  color: white;
  font-size: 14px;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.input-group input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.input-group input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.25);
  border-color: rgba(255, 255, 255, 0.5);
}

.map-section {
  margin: 20px 0;
}

.map-label {
  display: block;
  color: white;
  font-size: 13px;
  margin-bottom: 10px;
  text-align: center;
}

.map-container {
  height: 250px;
  width: 100%;
  border-radius: 15px;
  overflow: hidden;
  border: 2px solid rgba(255, 255, 255, 0.3);
}


.coords-container {
  display: flex;
  align-items: center;
  justify-content: center;
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.1);
  padding: 12px 15px;
  border-radius: 12px;
  margin-top: 15px;
  gap: 20px;
}

.coord-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.coord-label {
  font-size: 10px;
  font-weight: 700;
  color: rgba(255, 255, 255, 0.6);
  letter-spacing: 1px;
  margin-bottom: 2px;
}

.coord-value {
  font-family: 'Courier New', monospace; /* Estilo técnico */
  font-size: 14px;
  color: #fff;
  font-weight: 600;
  text-shadow: 0 1px 2px rgba(0,0,0,0.5);
}

.coord-divider {
  width: 1px;
  height: 24px;
  background: rgba(255, 255, 255, 0.2);
}
/* ------------------------------------------- */

.btn-register {
  width: 100%;
  padding: 15px;
  background: linear-gradient(135deg, #ff6b35 0%, #f7931e 100%);
  border: none;
  border-radius: 50px;
  color: white;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 1px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 15px;
}

.btn-register:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 20px rgba(255, 107, 53, 0.5);
}

.btn-register:active {
  transform: translateY(0);
}

.error {
  color: #ff6b6b;
  text-align: center;
  margin-top: 15px;
  font-size: 13px;
  background: rgba(255, 107, 107, 0.2);
  padding: 10px;
  border-radius: 10px;
}

.footer-link {
  text-align: center;
  margin-top: 20px;
}

.btn-link {
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 14px;
  text-decoration: underline;
  transition: opacity 0.3s ease;
}

.btn-link:hover {
  opacity: 0.8;
}

.auth-card::-webkit-scrollbar {
  width: 8px;
}

.auth-card::-webkit-scrollbar-track {
  background: rgba(255, 255, 255, 0.1);
  border-radius: 10px;
}

.auth-card::-webkit-scrollbar-thumb {
  background: rgba(255, 255, 255, 0.3);
  border-radius: 10px;
}

@media (max-width: 480px) {
  .auth-card {
    padding: 25px 20px;
  }

  .auth-header h2 {
    font-size: 20px;
  }

  .map-container {
    height: 200px;
  }
}
</style>