<template>
  <nav class="navbar">
    <div class="nav-container">
      <div class="nav-brand" @click="irAInicio">
        <div class="logo">📋</div>
        <span class="brand-name">Gestión de Tareas</span>
      </div>

      <div class="nav-menu" :class="{ active: menuActive }">
        <router-link
            to="/tareas"
            class="nav-link"
            @click="cerrarMenu"
            active-class="active"
        >
          <span class="nav-icon">📋</span>
          <span>Mis Tareas</span>
        </router-link>

        <router-link
            to="/analisis"
            class="nav-link"
            @click="cerrarMenu"
            active-class="active"
        >
          <span class="nav-icon">📈</span>
          <span>Análisis</span>
        </router-link>
      </div>

      <div class="nav-actions">
        <div class="user-menu" @click="toggleUserMenu">
          <div class="user-avatar">{{ iniciales }}</div>
          <span class="user-name">{{ nombreUsuario }}</span>
          <span class="dropdown-icon" :class="{ open: showUserMenu }">▼</span>
        </div>

        <transition name="dropdown">
          <div v-if="showUserMenu" class="user-dropdown" v-click-outside="cerrarUserMenu">
            <div class="user-info">
              <div class="user-avatar-large">{{ iniciales }}</div>
              <div>
                <div class="user-dropdown-name">{{ nombreUsuario }}</div>
                <div class="user-dropdown-email">Usuario activo</div>
              </div>
            </div>
            <div class="dropdown-divider"></div>
            <div class="dropdown-item" @click="cerrarSesion">
              <span class="dropdown-icon-item">⎋</span>
              <span>Cerrar Sesión</span>
            </div>
          </div>
        </transition>
      </div>

      <button
          class="menu-toggle"
          :class="{ active: menuActive }"
          @click="toggleMenu"
          aria-label="Toggle menu"
      >
        <span></span>
        <span></span>
        <span></span>
      </button>
    </div>
  </nav>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const menuActive = ref(false);
const showUserMenu = ref(false);

// Obtener información del usuario desde localStorage
const obtenerUsuario = () => {
  try {
    const usuarioStr = localStorage.getItem('usuario');
    return usuarioStr ? JSON.parse(usuarioStr) : null;
  } catch (e) {
    return null;
  }
};

const usuario = computed(() => obtenerUsuario());
const nombreUsuario = computed(() => usuario.value?.nombreUsuario || 'Usuario');

const iniciales = computed(() => {
  const nombre = nombreUsuario.value;
  if (nombre.length >= 2) {
    return nombre.substring(0, 2).toUpperCase();
  }
  return nombre.substring(0, 1).toUpperCase();
});

const toggleMenu = () => {
  menuActive.value = !menuActive.value;
};

const cerrarMenu = () => {
  menuActive.value = false;
};

const toggleUserMenu = () => {
  showUserMenu.value = !showUserMenu.value;
};

const cerrarUserMenu = () => {
  showUserMenu.value = false;
};

const irAInicio = () => {
  router.push('/tareas');
  cerrarMenu();
};

const cerrarSesion = () => {
  if (confirm('¿Estás seguro de cerrar sesión?')) {
    // Limpiar localStorage
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');

    // Redirigir al login
    router.push('/login');
  }
  cerrarUserMenu();
};

// Directiva personalizada para click outside
const vClickOutside = {
  mounted(el, binding) {
    el.clickOutsideEvent = (event) => {
      if (!(el === event.target || el.contains(event.target))) {
        binding.value();
      }
    };
    document.addEventListener('click', el.clickOutsideEvent);
  },
  unmounted(el) {
    document.removeEventListener('click', el.clickOutsideEvent);
  },
};
</script>

<style scoped>
.navbar {
  background: var(--bg-card);
  border-bottom: 1px solid var(--border-color);
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.3);
}

.nav-container {
  max-width: 1400px;
  margin: 0 auto;
  padding: 0 30px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: 70px;
}

/* Brand */
.nav-brand {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  transition: transform 0.3s ease;
  user-select: none;
}

.nav-brand:hover {
  transform: scale(1.05);
}

.logo {
  font-size: 32px;
  line-height: 1;
}

.brand-name {
  font-size: 22px;
  font-weight: 800;
  background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* Navigation Menu */
.nav-menu {
  display: flex;
  gap: 8px;
  align-items: center;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 20px;
  color: var(--text-secondary);
  text-decoration: none;
  border-radius: 10px;
  font-weight: 600;
  font-size: 15px;
  transition: all 0.3s ease;
  position: relative;
}

.nav-link:hover {
  color: var(--primary-color);
  background: rgba(0, 212, 255, 0.1);
  transform: translateY(-2px);
}

.nav-link.active {
  color: var(--primary-color);
  background: rgba(0, 212, 255, 0.15);
}

.nav-link.active::after {
  content: '';
  position: absolute;
  bottom: -16px;
  left: 50%;
  transform: translateX(-50%);
  width: 60%;
  height: 3px;
  background: linear-gradient(90deg, var(--primary-color), var(--accent-color));
  border-radius: 2px;
}

.nav-icon {
  font-size: 18px;
}

/* User Actions */
.nav-actions {
  position: relative;
}

.user-menu {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 18px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  user-select: none;
}

.user-menu:hover {
  border-color: var(--primary-color);
  box-shadow: 0 4px 12px rgba(0, 212, 255, 0.2);
  transform: translateY(-2px);
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  color: white;
  font-size: 14px;
  flex-shrink: 0;
}

.user-name {
  font-weight: 600;
  color: var(--text-primary);
  white-space: nowrap;
}

.dropdown-icon {
  font-size: 10px;
  color: var(--text-secondary);
  transition: transform 0.3s ease;
}

.dropdown-icon.open {
  transform: rotate(180deg);
}

/* User Dropdown */
.user-dropdown {
  position: absolute;
  top: calc(100% + 12px);
  right: 0;
  background: var(--bg-card);
  border: 1px solid var(--border-color);
  border-radius: 12px;
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.5);
  min-width: 260px;
  overflow: hidden;
  z-index: 1001;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 16px 20px;
  background: var(--bg-secondary);
}

.user-avatar-large {
  width: 48px;
  height: 48px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  color: white;
  font-size: 18px;
  flex-shrink: 0;
}

.user-dropdown-name {
  font-weight: 600;
  color: var(--text-primary);
  font-size: 15px;
}

.user-dropdown-email {
  font-size: 12px;
  color: var(--text-secondary);
  margin-top: 2px;
}

.dropdown-divider {
  height: 1px;
  background: var(--border-color);
  margin: 0;
}

.dropdown-item {
  padding: 14px 20px;
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  color: var(--text-primary);
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
}

.dropdown-item:hover {
  background: rgba(0, 212, 255, 0.1);
  color: var(--primary-color);
}

.dropdown-icon-item {
  font-size: 18px;
  width: 24px;
  display: flex;
  align-items: center;
  justify-content: center;
}

/* Dropdown Transition */
.dropdown-enter-active,
.dropdown-leave-active {
  transition: all 0.3s ease;
}

.dropdown-enter-from {
  opacity: 0;
  transform: translateY(-10px);
}

.dropdown-leave-to {
  opacity: 0;
  transform: translateY(-10px);
}

/* Menu Toggle (Mobile) */
.menu-toggle {
  display: none;
  flex-direction: column;
  gap: 5px;
  background: none;
  border: none;
  cursor: pointer;
  padding: 8px;
  width: 40px;
  height: 40px;
  justify-content: center;
  align-items: center;
}

.menu-toggle span {
  width: 25px;
  height: 3px;
  background: var(--text-primary);
  border-radius: 2px;
  transition: all 0.3s ease;
  display: block;
}

.menu-toggle.active span:nth-child(1) {
  transform: rotate(45deg) translate(6px, 6px);
}

.menu-toggle.active span:nth-child(2) {
  opacity: 0;
  transform: translateX(-10px);
}

.menu-toggle.active span:nth-child(3) {
  transform: rotate(-45deg) translate(6px, -6px);
}

/* Responsive */
@media (max-width: 768px) {
  .nav-container {
    padding: 0 20px;
    height: 65px;
  }

  .brand-name {
    font-size: 18px;
  }

  .logo {
    font-size: 28px;
  }

  .nav-menu {
    position: fixed;
    top: 65px;
    left: 0;
    right: 0;
    flex-direction: column;
    background: var(--bg-card);
    border-bottom: 1px solid var(--border-color);
    padding: 20px;
    gap: 10px;
    transform: translateX(-100%);
    opacity: 0;
    transition: all 0.3s ease;
    pointer-events: none;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
  }

  .nav-menu.active {
    transform: translateX(0);
    opacity: 1;
    pointer-events: all;
  }

  .nav-link {
    width: 100%;
    justify-content: flex-start;
  }

  .nav-link.active::after {
    display: none;
  }

  .user-name {
    display: none;
  }

  .dropdown-icon {
    display: none;
  }

  .user-menu {
    padding: 8px;
  }

  .menu-toggle {
    display: flex;
  }

  .user-dropdown {
    right: -10px;
  }
}

@media (max-width: 480px) {
  .nav-container {
    padding: 0 15px;
  }

  .brand-name {
    font-size: 16px;
  }

  .user-dropdown {
    right: -15px;
    min-width: 240px;
  }
}
</style>