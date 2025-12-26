import axios from 'axios';

// Usa variable de entorno si existe; por defecto apunta al puerto expuesto por docker-compose
const API_BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8081/api';

const apiClient = axios.create({
    baseURL: API_BASE_URL,
    headers: {
        'Content-Type': 'application/json',
    },
});

// Interceptor: Agrega el token a cada petición si existe
apiClient.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
}, (error) => {
    return Promise.reject(error);
});

// Interceptor de respuesta: Maneja errores de autenticación
let isRedirecting = false;

apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
        // Manejar errores de autenticación (401 o 403)
        if (error.response && (error.response.status === 401 || error.response.status === 403)) {
            // Token inválido, expirado o sin permisos
            if (!isRedirecting && window.location.pathname !== '/login') {
                isRedirecting = true;
                localStorage.removeItem('token');
                localStorage.removeItem('usuario');

                console.error('Token inválido, expirado o sin permisos. Redirigiendo al login...');

                // Redirigir al login
                setTimeout(() => {
                    window.location.href = '/login';
                }, 100);
            }
        }
        // Para otros errores, solo rechazar sin redirigir
        return Promise.reject(error);
    }
);

export default apiClient;