<template>
  <div class="auth-page">
    <div class="auth-card">
      <div class="auth-header">
        <h2>INICIAR SESIÓN</h2>
      </div>

      <form @submit.prevent="handleLogin">
        <div class="input-group">
          <input
              v-model="nombreUsuario"
              type="text"
              placeholder="Ingresa tu usuario"
              required
          />
        </div>

        <div class="input-group">
          <div class="password-wrapper">
            <input
                v-model="contrasena"
                :type="mostrarContrasena ? 'text' : 'password'"
                placeholder="Ingresa tu contraseña"
                required
            />
            <button
                type="button"
                class="toggle-password"
                @click="mostrarContrasena = !mostrarContrasena"
                :aria-label="mostrarContrasena ? 'Ocultar contraseña' : 'Mostrar contraseña'"
            >
              <span v-if="mostrarContrasena">🙉</span>
              <span v-else>🙈</span>
            </button>
          </div>
        </div>

        <div class="remember-me">
          <label>
            <input type="checkbox" />
            <span>Mantener sesión iniciada en este dispositivo</span>
          </label>
        </div>

        <button type="submit" class="btn-login">INICIAR SESIÓN</button>
      </form>

      <div class="footer-link">
        <button @click="$router.push('/registro')" class="btn-link">
          ¿No tienes cuenta? Regístrate
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import authService from '../api/auth.service';
import { useAuthStore } from '@/stores/auth.store';
import { useRouter } from 'vue-router';

const nombreUsuario = ref('');
const contrasena = ref('');
const mostrarContrasena = ref(false);
const authStore = useAuthStore();
const router = useRouter();

const handleLogin = async () => {
  try {
    const response = await authService.login({
      nombreUsuario: nombreUsuario.value,
      contrasena: contrasena.value
    });
    authStore.setAuth(response.data.token, response.data.usuario);
    router.push('/tareas');
  } catch (e) {
    alert('Credenciales incorrectas');
  }
};
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)),
  url('@/assets/images/login-bg.jpg'); /* Asegúrate de que esta imagen exista o cambia la ruta */
  background-size: cover;
  background-position: center;
  background-attachment: fixed;
  padding: 20px;
}

.auth-card {
  background: rgba(255, 255, 255, 0.08);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.15);
  border-radius: 20px;
  padding: 40px 35px;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 8px 32px 0 rgba(0, 0, 0, 0.3);
}

.auth-header {
  text-align: center;
  margin-bottom: 35px;
}

.auth-header h2 {
  color: white;
  font-size: 24px;
  font-weight: 600;
  letter-spacing: 1px;
  margin: 0;
}

.input-group {
  margin-bottom: 20px;
}

.password-wrapper {
  position: relative;
  display: flex;
  align-items: center;
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

.password-wrapper input {
  padding-right: 55px;
}

.toggle-password {
  position: absolute;
  right: 14px;
  background: none;
  border: none;
  color: white;
  cursor: pointer;
  font-size: 18px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.toggle-password:hover {
  opacity: 0.8;
}

.input-group input::placeholder {
  color: rgba(255, 255, 255, 0.7);
}

.input-group input:focus {
  outline: none;
  background: rgba(255, 255, 255, 0.25);
  border-color: rgba(255, 255, 255, 0.5);
}


.remember-me {
  margin: 20px 0;
  text-align: center;
}

.remember-me label {
  display: flex; /* Usamos flexbox para alinear la caja y el texto */
  align-items: center; /* Centrado vertical */
  justify-content: center; /* Centrado horizontal */
  gap: 10px; /* Espacio entre el checkbox y el texto */
  color: white;
  font-size: 13px;
  cursor: pointer;
  user-select: none;
}

.remember-me input[type="checkbox"] {
  width: auto !important; /* Importante: anula el 100% global */
  margin: 0 !important;   /* Quitamos márgenes extraños */
  cursor: pointer;
  width: 16px;
  height: 16px;
  accent-color: #ff6b35; /* Color naranja al marcarlo */
}
/* -------------------------------------- */

.btn-login {
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
  margin-top: 10px;
}

.btn-login:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 20px rgba(255, 107, 53, 0.5);
}

.btn-login:active {
  transform: translateY(0);
}

.footer-link {
  text-align: center;
  margin-top: 25px;
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

/* Responsive */
@media (max-width: 480px) {
  .auth-card {
    padding: 30px 25px;
  }

  .auth-header h2 {
    font-size: 20px;
  }
}
</style>