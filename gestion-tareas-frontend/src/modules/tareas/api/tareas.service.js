import apiClient from '@/core/api/axios';

export default {
    obtenerTodas() {
        return apiClient.get('/tareas');
    },
    crear(tarea) {
        return apiClient.post('/tareas', tarea);
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