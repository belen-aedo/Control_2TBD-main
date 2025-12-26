import axios from '@/core/api/axios';

const API_URL = '/tareas/analisis';

export default {
    // Obtener promedio de distancia de tareas completadas
    getPromedioDistancia() {
        return axios.get(`${API_URL}/promedio-distancia`);
    },

    // Obtener tarea pendiente más cercana al usuario
    getTareaMasCercana() {
        return axios.get(`${API_URL}/mas-cercana`);
    },

    // Obtener cantidad de tareas por sector
    getTareasPorSector() {
        return axios.get(`${API_URL}/por-sector`);
    },

    // Obtener sector con más tareas completadas en 2 km
    getSectorMasTareas2km() {
        return axios.get(`${API_URL}/sector-mas-tareas-2km`);
    },

    // Obtener sector con más tareas completadas en 5 km
    getSectorMasTareas5km() {
        return axios.get(`${API_URL}/sector-mas-tareas-5km`);
    },

    // Obtener concentración de tareas pendientes
    getConcentracionPendientes() {
        return axios.get(`${API_URL}/concentracion-pendientes`);
    },
};