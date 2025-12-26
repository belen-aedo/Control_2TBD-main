<script setup>
import { computed } from 'vue';
import { useRoute } from 'vue-router';
import Navbar from '@/modules/core/components/Navbar.vue';

const route = useRoute();

// Mostrar navbar solo si NO estamos en login o registro
const mostrarNavbar = computed(() => {
  const rutasSinNavbar = ['login', 'registro', 'Login', 'Registro'];
  return !rutasSinNavbar.includes(route.name);
});
</script>

<template>
  <div id="app-container">
    <Navbar v-if="mostrarNavbar" />
    <main :class="{ 'with-navbar': mostrarNavbar }">
      <router-view></router-view>
    </main>
  </div>
</template>

<style>
/* Variables globales del tema oscuro */
:root {
  --bg-primary: #0a0e27;
  --bg-secondary: #151932;
  --bg-card: #1a1f3a;
  --bg-hover: #222847;

  --primary-color: #ff8c42;
  --primary-dark: #ff6b1a;
  --accent-color: #ffa726;
  --success: #00ff88;
  --warning: #ffd700;
  --danger: #ff4757;

  --text-primary: #ffffff;
  --text-secondary: #a0aec0;
  --text-muted: #718096;

  --border-color: #2d3548;
  --shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
  --shadow-lg: 0 10px 25px rgba(0, 0, 0, 0.5);
}

/* Reset global */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  margin: 0;
  padding: 0;
  width: 100%;
  min-height: 100%;
  overflow-x: hidden;
}

body {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', 'Roboto', sans-serif;
  background: var(--bg-primary);
  color: var(--text-primary);
  line-height: 1.6;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* Contenedor principal */
#app-container {
  width: 100%;
  min-height: 100vh;
  margin: 0;
  padding: 0;
  background: var(--bg-primary);
}

/* Main content con navbar */
main.with-navbar {
  padding-top: 0; /* El navbar ya tiene su altura */
}

/* Páginas de autenticación (sin navbar) */
.auth-page {
  margin: 0 !important;
  padding: 0 !important;
  min-height: 100vh;
}

/* Cards globales */
.card {
  background: var(--bg-card);
  border-radius: 16px;
  padding: 24px;
  border: 1px solid var(--border-color);
  box-shadow: var(--shadow);
  transition: all 0.3s ease;
}

.card:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

/* Buttons globales */
button, .btn {
  background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 10px;
  font-size: 14px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
  font-family: inherit;
}

button:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 212, 255, 0.5);
}

button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.btn-secondary {
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  box-shadow: none;
}

.btn-secondary:hover {
  background: var(--bg-hover);
  border-color: var(--primary-color);
}

.btn-danger {
  background: linear-gradient(135deg, #ff4757, #ff6b81);
}

.btn-success {
  background: linear-gradient(135deg, #00ff88, #00d4aa);
}

/* Form elements globales */
input, textarea, select {
  width: 100%;
  padding: 12px 16px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 10px;
  color: var(--text-primary);
  font-size: 14px;
  font-family: inherit;
  transition: all 0.3s ease;
}

input:focus, textarea:focus, select:focus {
  outline: none;
  border-color: var(--primary-color);
  box-shadow: 0 0 0 3px rgba(0, 212, 255, 0.1);
}

input::placeholder, textarea::placeholder {
  color: var(--text-muted);
}

/* Scrollbar */
::-webkit-scrollbar {
  width: 10px;
}

::-webkit-scrollbar-track {
  background: var(--bg-secondary);
}

::-webkit-scrollbar-thumb {
  background: var(--primary-color);
  border-radius: 5px;
}

::-webkit-scrollbar-thumb:hover {
  background: var(--primary-dark);
}

/* Utilidades */
.text-center {
  text-align: center;
}

.mt-4 {
  margin-top: 1rem;
}

.mb-4 {
  margin-bottom: 1rem;
}

/* Animaciones */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.fade-in {
  animation: fadeIn 0.5s ease-out;
}
</style>