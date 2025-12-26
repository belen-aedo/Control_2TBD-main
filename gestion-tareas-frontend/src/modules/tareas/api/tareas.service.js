import apiClient from '@/core/api/axios';

export default {
    obtenerTodas() {
        return apiClient.get('/tareas');
    },
    crear(tarea) {
        return apiClient.post('/tareas', tarea);
    },
    obtenerPorId(id) {
        return apiClient.get(`/tareas/${id}`);
    },
    actualizar(id, tarea) {
        return apiClient.put(`/tareas/${id}`, tarea);
    },
    eliminar(id) {
        return apiClient.delete(`/tareas/${id}`);
    },
    completar(id) {
        return apiClient.patch(`/tareas/${id}/completar`);
    }
};