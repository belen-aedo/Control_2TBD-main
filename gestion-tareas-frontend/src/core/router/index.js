import { createRouter, createWebHistory } from 'vue-router';
import LoginView from '@/modules/auth/views/LoginView.vue';
import RegistroView from '@/modules/auth/views/RegistroView.vue';
import { useAuthStore } from '@/stores/auth.store';

const routes = [
    { path: '/', redirect: '/login' },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/registro', name: 'registro', component: RegistroView },

    // Tareas
    {
        path: '/tareas',
        component: () => import('@/modules/tareas/views/ListaTareas.vue'),
        meta: { requiresAuth: true }
    },
    {
        path: '/tareas/nueva',
        component: () => import('@/modules/tareas/views/FormularioTarea.vue'),
        meta: { requiresAuth: true }
    },

    // Análisis
    {
        path: '/analisis',
        component: () => import('@/modules/analisis/views/AnalisisView.vue'),
        meta: { requiresAuth: true }
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();
    if (to.meta.requiresAuth && !authStore.token) {
        next('/login');
    } else {
        next();
    }
});

export default router;